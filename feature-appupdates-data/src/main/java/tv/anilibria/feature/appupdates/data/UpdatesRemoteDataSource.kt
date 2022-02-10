package tv.anilibria.feature.appupdates.data

import com.squareup.moshi.Moshi
import io.reactivex.Single
import tv.anilibria.feature.appupdates.data.domain.UpdateData
import tv.anilibria.feature.appupdates.data.response.UpdateDataResponse
import tv.anilibria.plugin.data.network.NetworkClient
import tv.anilibria.plugin.data.restapi.ApiClient
import tv.anilibria.plugin.data.restapi.ApiConfigProvider
import tv.anilibria.plugin.data.restapi.MainClient
import tv.anilibria.plugin.data.restapi.mapApiResponse
import javax.inject.Inject

class UpdatesRemoteDataSource @Inject constructor(
    @ApiClient private val apiClient: NetworkClient,
    @MainClient private val mainClient: NetworkClient,
    private val apiConfig: ApiConfigProvider,
    private val moshi: Moshi,
    private val reserveSources: CheckerReserveSources
) {

    private val updatesAdapter by lazy {
        moshi.adapter(UpdateDataResponse::class.java)
    }

    fun checkUpdate(versionCode: Int): Single<UpdateData> {
        val args = mapOf(
            "query" to "app_update",
            "current" to versionCode.toString()
        )
        return apiClient.post(apiConfig.apiUrl, args)
            .mapApiResponse<UpdateData>(moshi)
            .onErrorResumeNext { getUpdatesFromReserve() }
    }

    private fun getUpdatesFromReserve(): Single<UpdateData> {
        val singleSources = reserveSources.sources.map { source ->
            getReserve(source)
                .map { Result.success(it) }
                .onErrorReturn { Result.failure(it) }
        }
        return Single.merge(singleSources)
            .filter { it.isSuccess }
            .map { it.getOrThrow() }
            .firstOrError()
    }

    private fun getReserve(url: String): Single<UpdateData> =
        mainClient
            .get(url, emptyMap())
            .map { updatesAdapter.fromJson(it) }
            .map { it.toDomain() }
}