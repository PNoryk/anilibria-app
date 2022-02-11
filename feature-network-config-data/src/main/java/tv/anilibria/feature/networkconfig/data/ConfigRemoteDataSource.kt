package tv.anilibria.feature.networkconfig.data

import io.reactivex.Single
import ru.radiationx.shared.ktx.SchedulersProvider
import tv.anilibria.feature.networkconfig.data.domain.ApiAddress
import tv.anilibria.plugin.data.network.formBodyOf
import tv.anilibria.plugin.data.restapi.ApiWrapper
import tv.anilibria.plugin.data.restapi.handleApiResponse
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ConfigRemoteDataSource @Inject constructor(
    private val configApi: ApiWrapper<ConfigApi>,
    private val schedulers: SchedulersProvider,
    private val reserveSources: ApiConfigReserveSources
) {

    fun checkAvailable(apiUrl: String): Single<Boolean> = check(configApi.direct(), apiUrl)
        .timeout(15, TimeUnit.SECONDS)

    fun checkApiAvailable(apiUrl: String): Single<Boolean> = check(configApi.proxy(), apiUrl)
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

    private fun check(client: ConfigApi, apiUrl: String): Single<Boolean> =
        client
            .checkAvailable(apiUrl, formBodyOf("query" to "empty"))
            .map { true }

    private fun getConfigFromApi(): Single<List<ApiAddress>> {
        val body = formBodyOf("query" to "config")
        return configApi.proxy()
            .getConfig(body)
            .handleApiResponse()
            .timeout(10, TimeUnit.SECONDS)
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
        configApi.direct()
            .getReserveConfig(url)
            .map { it.toDomain().addresses }
}