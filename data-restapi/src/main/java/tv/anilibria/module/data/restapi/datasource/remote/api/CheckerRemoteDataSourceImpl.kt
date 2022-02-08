package tv.anilibria.module.data.restapi.datasource.remote.api

import com.squareup.moshi.Moshi
import io.reactivex.Single
import tv.anilibria.module.data.restapi.ApiClient
import tv.anilibria.module.data.restapi.MainClient
import tv.anilibria.module.data.restapi.datasource.remote.IClient
import tv.anilibria.module.data.restapi.datasource.remote.ApiConfigProvider
import tv.anilibria.module.data.restapi.datasource.remote.common.CheckerReserveSources
import tv.anilibria.module.data.restapi.datasource.remote.mapApiResponse
import tv.anilibria.module.data.restapi.datasource.remote.mapResponse
import tv.anilibria.module.data.restapi.entity.app.updater.UpdateDataResponse
import tv.anilibria.module.data.restapi.entity.mapper.toDomain
import tv.anilibria.module.domain.entity.updater.UpdateData
import tv.anilibria.module.domain.remote.CheckerRemoteDataSource
import javax.inject.Inject

/**
 * Created by radiationx on 28.01.18.
 */
class CheckerRemoteDataSourceImpl @Inject constructor(
    @ApiClient private val client: IClient,
    @MainClient private val mainClient: IClient,
    private val apiConfig: ApiConfigProvider,
    private val reserveSources: CheckerReserveSources,
    private val moshi: Moshi
) : CheckerRemoteDataSource {

    override fun checkUpdate(versionCode: Int): Single<UpdateData> {
        val args = mapOf(
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
            .map { it.toDomain() }
    }

    private fun getReserve(url: String): Single<UpdateDataResponse> =
        mainClient
            .get(url, emptyMap())
            .mapResponse(moshi)
}