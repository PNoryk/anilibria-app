package tv.anilibria.module.data.network.datasource.remote.api

import io.reactivex.Single
import org.json.JSONObject
import tv.anilibria.module.data.network.ApiClient
import tv.anilibria.module.data.network.MainClient
import tv.anilibria.module.data.network.datasource.remote.ApiResponse
import tv.anilibria.module.data.network.datasource.remote.IClient
import tv.anilibria.module.data.network.datasource.remote.address.ApiConfig
import tv.anilibria.module.data.network.datasource.remote.common.CheckerReserveSources
import tv.anilibria.module.data.network.datasource.remote.parsers.CheckerParser
import tv.anilibria.module.data.network.entity.app.updater.UpdateDataResponse
import javax.inject.Inject

/**
 * Created by radiationx on 28.01.18.
 */
class CheckerApi @Inject constructor(
    @ApiClient private val client: IClient,
    @MainClient private val mainClient: IClient,
    private val checkerParser: CheckerParser,
    private val apiConfig: ApiConfig,
    private val reserveSources: CheckerReserveSources
) {

    fun checkUpdate(versionCode: Int): Single<UpdateDataResponse> {
        val args: MutableMap<String, String> = mutableMapOf(
            "query" to "app_update",
            "current" to versionCode.toString()
        )
        return client.post(apiConfig.apiUrl, args)
            .compose(ApiResponse.fetchResult<JSONObject>())
            .map { checkerParser.parse(it) }
            .onErrorResumeNext {
                var nextSingle: Single<UpdateDataResponse> = Single.error(it)
                reserveSources.sources.forEach { source ->
                    nextSingle = nextSingle.onErrorResumeNext { getReserve(source) }
                }
                nextSingle
            }
    }

    private fun getReserve(url: String): Single<UpdateDataResponse> =
        mainClient.get(url, emptyMap())
            .map { JSONObject(it) }
            .map { checkerParser.parse(it) }
}