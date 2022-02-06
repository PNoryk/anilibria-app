package tv.anilibria.module.data.network.datasource.remote.api

import io.reactivex.Single
import io.reactivex.functions.BiFunction
import org.json.JSONObject
import ru.radiationx.shared.ktx.SchedulersProvider
import tv.anilibria.module.data.network.ApiClient
import tv.anilibria.module.data.network.MainClient
import tv.anilibria.module.data.network.datasource.remote.ApiResponse
import tv.anilibria.module.data.network.datasource.remote.IClient
import tv.anilibria.module.data.network.datasource.remote.address.ApiAddressResponse
import tv.anilibria.module.data.network.datasource.remote.address.ApiConfig
import tv.anilibria.module.data.network.datasource.remote.parsers.ConfigurationParser
import tv.anilibria.module.data.network.datasource.storage.ApiConfigStorage
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ConfigurationApi @Inject constructor(
    @ApiClient private val client: IClient,
    @MainClient private val mainClient: IClient,
    private val configurationParser: ConfigurationParser,
    private val apiConfig: ApiConfig,
    private val apiConfigStorage: ApiConfigStorage,
    private val schedulers: SchedulersProvider
) {

    fun checkAvailable(apiUrl: String): Single<Boolean> = check(mainClient, apiUrl)
        .timeout(15, TimeUnit.SECONDS)

    fun checkApiAvailable(apiUrl: String): Single<Boolean> = check(client, apiUrl)
        .onErrorReturnItem(false)
        .timeout(15, TimeUnit.SECONDS)

    fun getConfiguration(): Single<List<ApiAddressResponse>> = getMergeConfig()
        .doOnSuccess {
            if (it.isEmpty()) {
                throw IllegalStateException("Empty config adresses")
            }
        }

    private fun check(client: IClient, apiUrl: String): Single<Boolean> =
        client.postFull(apiUrl, mapOf("query" to "empty"))
            .map { true }


    private fun getMergeConfig(): Single<List<ApiAddressResponse>> = Single
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

    private fun getZipConfig(): Single<List<ApiAddressResponse>> = Single
        .zip(
            getConfigFromApi()
                .subscribeOn(schedulers.io())
                .onErrorReturn { emptyList() },
            getConfigFromReserve()
                .subscribeOn(schedulers.io())
                .onErrorReturn { emptyList() },
            BiFunction<List<ApiAddressResponse>, List<ApiAddressResponse>, List<ApiAddressResponse>> { conf1, conf2 ->
                val addresses1 = conf1.takeIf { it.isNotEmpty() }
                val addresses2 = conf2.takeIf { it.isNotEmpty() }
                return@BiFunction (addresses1 ?: addresses2).orEmpty()
            }
        )


    private fun getConfigFromApi(): Single<List<ApiAddressResponse>> {
        val args = mapOf(
            "query" to "config"
        )
        return client.post(apiConfig.apiUrl, args)
            .timeout(10, TimeUnit.SECONDS)
            .compose(ApiResponse.fetchResult<JSONObject>())
            .doOnSuccess { apiConfigStorage.saveJson(it) }
            .map { configurationParser.parse(it).addresses }
            .doOnSuccess { apiConfig.setAddresses(it) }
    }

    private fun getConfigFromReserve(): Single<List<ApiAddressResponse>> {
        return getReserve("https://raw.githubusercontent.com/anilibria/anilibria-app/master/config.json")
            .onErrorResumeNext { getReserve("https://bitbucket.org/RadiationX/anilibria-app/raw/master/config.json") }
    }

    private fun getReserve(url: String): Single<List<ApiAddressResponse>> =
        mainClient.get(url, emptyMap())
            .map { JSONObject(it) }
            .doOnSuccess { apiConfigStorage.saveJson(it) }
            .map { configurationParser.parse(it).addresses }
            .doOnSuccess { apiConfig.setAddresses(it) }

}