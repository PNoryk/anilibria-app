package tv.anilibria.feature.networkconfig.data

import com.squareup.moshi.Moshi
import io.reactivex.Single
import ru.radiationx.shared.ktx.SchedulersProvider
import tv.anilibria.feature.networkconfig.data.domain.ApiAddress
import tv.anilibria.feature.networkconfig.data.response.ApiConfigResponse
import tv.anilibria.module.data.network.NetworkClient
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ConfigRemoteDataSource @Inject constructor(
    private val mainClient: NetworkClient,
    private val schedulers: SchedulersProvider,
    private val moshi: Moshi,
    private val reserveSources: ApiConfigReserveSources,
    private val appDataSource: AppConfigRemoteDataSource
) {

    private val configAdapter by lazy {
        moshi.adapter(ApiConfigResponse::class.java)
    }

    fun checkAvailable(apiUrl: String): Single<Boolean> =
        appDataSource
            .checkAvailable(apiUrl)
            .timeout(15, TimeUnit.SECONDS)

    fun checkApiAvailable(apiUrl: String): Single<Boolean> =
        appDataSource
            .checkApiAvailable(apiUrl)
            .onErrorReturnItem(false)
            .timeout(15, TimeUnit.SECONDS)

    fun getConfiguration(): Single<List<ApiAddress>> = Single
        .merge(
            appDataSource
                .getConfiguration()
                .map { it.toDomain().addresses }
                .subscribeOn(schedulers.io())
                .onErrorReturn { emptyList() },
            getConfigFromReserve()
                .subscribeOn(schedulers.io())
                .onErrorReturn { emptyList() }
        )
        .filter { it.isNotEmpty() }
        .first(emptyList())
        .doOnSuccess {
            if (it.isEmpty()) {
                throw IllegalStateException("Empty config adresses")
            }
        }

    private fun getConfigFromReserve(): Single<List<ApiAddress>> {
        val singleSources = reserveSources.sources.map { source ->
            getReserve(source)
                .map { Result.success(it) }
                .onErrorReturn { Result.failure(it) }
        }
        return Single.merge(singleSources)
            .filter { it.isSuccess }
            .map { it.getOrThrow() }
            .filter { it.isNotEmpty() }
            .firstOrError()
    }

    private fun getReserve(url: String): Single<List<ApiAddress>> =
        mainClient.get(url, emptyMap())
            .map { configAdapter.fromJson(it) }
            .map { it.toDomain().addresses }
}