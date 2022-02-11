package tv.anilibria.feature.networkconfig.data

import com.squareup.moshi.Moshi
import io.reactivex.Single
import ru.radiationx.shared.ktx.SchedulersProvider
import tv.anilibria.feature.networkconfig.data.domain.ApiAddress
import tv.anilibria.feature.networkconfig.data.response.ApiConfigResponse
import tv.anilibria.plugin.data.network.NetworkClient
import tv.anilibria.plugin.data.restapi.ApiNetworkClient
import tv.anilibria.plugin.data.restapi.ApiConfigProvider
import tv.anilibria.plugin.data.restapi.DefaultNetworkClient
import tv.anilibria.plugin.data.restapi.mapApiResponse
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ConfigRemoteDataSource @Inject constructor(
    private val apiClient: ApiNetworkClient,
    private val mainClient: DefaultNetworkClient,
    private val schedulers: SchedulersProvider,
    private val moshi: Moshi,
    private val apiConfig: ApiConfigProvider,
    private val reserveSources: ApiConfigReserveSources
) {

    private val configAdapter by lazy {
        moshi.adapter(ApiConfigResponse::class.java)
    }

    fun checkAvailable(apiUrl: String): Single<Boolean> = check(mainClient, apiUrl)
        .timeout(15, TimeUnit.SECONDS)

    fun checkApiAvailable(apiUrl: String): Single<Boolean> = check(apiClient, apiUrl)
        .onErrorReturnItem(false)
        .timeout(15, TimeUnit.SECONDS)

    fun getConfiguration(): Single<List<ApiAddress>> = Single
        .merge(
            getConfigFromApi()
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

    private fun check(client: NetworkClient, apiUrl: String): Single<Boolean> =
        client.post(apiUrl, mapOf("query" to "empty"))
            .map { true }

    private fun getConfigFromApi(): Single<List<ApiAddress>> {
        val args = mapOf(
            "query" to "config"
        )
        return apiClient.post(apiConfig.apiUrl, args)
            .timeout(10, TimeUnit.SECONDS)
            .mapApiResponse<ApiConfigResponse>(moshi)
            .map { it.toDomain().addresses }
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
            .map { configAdapter.fromJson(it.body) }
            .map { it.toDomain().addresses }
}