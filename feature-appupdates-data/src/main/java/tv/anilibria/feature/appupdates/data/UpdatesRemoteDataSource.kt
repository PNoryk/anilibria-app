package tv.anilibria.feature.appupdates.data

import io.reactivex.Single
import tv.anilibria.feature.appupdates.data.domain.UpdateData
import tv.anilibria.plugin.data.network.formBodyOf
import tv.anilibria.plugin.data.restapi.ApiWrapper
import tv.anilibria.plugin.data.restapi.handleApiResponse
import javax.inject.Inject

class UpdatesRemoteDataSource @Inject constructor(
    private val updaterApi: ApiWrapper<UpdaterApi>,
    private val reserveSources: CheckerReserveSources
) {

    fun checkUpdate(versionCode: Int): Single<UpdateData> {
        val args = formBodyOf(
            "query" to "app_update",
            "current" to versionCode.toString()
        )
        return updaterApi.proxy().checkUpdate(args)
            .handleApiResponse()
            .map { it.toDomain() }
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
        updaterApi
            .direct()
            .checkReserve(url)
            .map { it.toDomain() }
}