package tv.anilibria.module.data.network.datasource.remote.api

import com.squareup.moshi.Moshi
import io.reactivex.Single
import tv.anilibria.module.data.network.ApiClient
import tv.anilibria.module.data.network.MainClient
import tv.anilibria.module.data.network.datasource.remote.IClient
import tv.anilibria.module.data.network.datasource.remote.address.ApiConfigProvider
import tv.anilibria.module.data.network.datasource.remote.common.CheckerReserveSources
import tv.anilibria.module.data.network.datasource.remote.mapApiResponse
import tv.anilibria.module.data.network.datasource.remote.mapResponse
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
    private val apiConfig: ApiConfigProvider,
    private val reserveSources: CheckerReserveSources,
    private val moshi: Moshi
) {

    fun checkUpdate(versionCode: Int): Single<UpdateDataResponse> {
        val args: MutableMap<String, String> = mutableMapOf(
            "query" to "app_update",
            "current" to versionCode.toString()
        )
        return client
            .post(apiConfig.apiUrl, args)
            .mapApiResponse<UpdateDataResponse>(moshi)
            .onErrorResumeNext {
                var nextSingle: Single<UpdateDataResponse> = Single.error(it)
                reserveSources.sources.forEach { source ->
                    nextSingle = nextSingle.onErrorResumeNext { getReserve(source) }
                }
                nextSingle
            }
    }

    private fun getReserve(url: String): Single<UpdateDataResponse> =
        mainClient
            .get(url, emptyMap())
            .mapResponse(moshi)
}