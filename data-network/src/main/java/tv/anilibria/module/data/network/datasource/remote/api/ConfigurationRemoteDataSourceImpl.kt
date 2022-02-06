package tv.anilibria.module.data.network.datasource.remote.api

import com.squareup.moshi.Moshi
import io.reactivex.Single
import ru.radiationx.shared.ktx.SchedulersProvider
import tv.anilibria.module.data.network.ApiClient
import tv.anilibria.module.data.network.MainClient
import tv.anilibria.module.data.network.datasource.remote.IClient
import tv.anilibria.module.data.network.datasource.remote.address.ApiConfigProvider
import tv.anilibria.module.data.network.datasource.remote.address.ApiConfigResponse
import tv.anilibria.module.data.network.datasource.remote.mapApiResponse
import tv.anilibria.module.data.network.datasource.remote.mapResponse
import tv.anilibria.module.data.network.entity.mapper.toDomain
import tv.anilibria.module.domain.entity.address.ApiAddress
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ConfigurationRemoteDataSourceImpl @Inject constructor(
    @ApiClient private val client: IClient,
    @MainClient private val mainClient: IClient,
    private val apiConfig: ApiConfigProvider,
    private val schedulers: SchedulersProvider,
    private val moshi: Moshi
) : ConfigurationRemoteDataSource {

    override fun checkAvailable(apiUrl: String): Single<Boolean> = check(mainClient, apiUrl)
        .timeout(15, TimeUnit.SECONDS)

    override fun checkApiAvailable(apiUrl: String): Single<Boolean> = check(client, apiUrl)
        .onErrorReturnItem(false)
        .timeout(15, TimeUnit.SECONDS)

    override fun getConfiguration(): Single<List<ApiAddress>> = getMergeConfig()
        .doOnSuccess {
            if (it.isEmpty()) {
                throw IllegalStateException("Empty config adresses")
            }
        }

    override fun check(client: IClient, apiUrl: String): Single<Boolean> =
        client.postFull(apiUrl, mapOf("query" to "empty"))
            .map { true }

    override fun getMergeConfig(): Single<List<ApiAddress>> = Single
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

    override fun getConfigFromApi(): Single<List<ApiAddress>> {
        val args = mapOf(
            "query" to "config"
        )
        return client.post(apiConfig.apiUrl, args)
            .timeout(10, TimeUnit.SECONDS)
            .mapApiResponse<ApiConfigResponse>(moshi)
            .map { it.toDomain() }
            .map { it.addresses }
    }

    override fun getConfigFromReserve(): Single<List<ApiAddress>> {
        return getReserve("https://raw.githubusercontent.com/anilibria/anilibria-app/master/config.json")
            .onErrorResumeNext { getReserve("https://bitbucket.org/RadiationX/anilibria-app/raw/master/config.json") }
    }

    override fun getReserve(url: String): Single<List<ApiAddress>> =
        mainClient.get(url, emptyMap())
            .mapResponse<ApiConfigResponse>(moshi)
            .map { it.toDomain() }
            .map { it.addresses }
}