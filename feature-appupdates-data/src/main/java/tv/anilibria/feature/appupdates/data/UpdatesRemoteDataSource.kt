package tv.anilibria.feature.appupdates.data

import tv.anilibria.feature.appupdates.data.domain.UpdateData
import tv.anilibria.plugin.data.network.formBodyOf
import tv.anilibria.plugin.data.network.NetworkWrapper
import tv.anilibria.plugin.data.restapi.handleApiResponse
import javax.inject.Inject

class UpdatesRemoteDataSource @Inject constructor(
    private val updaterApi: NetworkWrapper<UpdaterApi>,
    private val reserveSources: CheckerReserveSources
) {

    suspend fun checkUpdate(versionCode: Int): UpdateData {
        return runCatching { getUpdatesFromApi(versionCode) }
            .getOrNull()
            ?: getUpdatesFromReserve()
            ?: throw IllegalStateException("Error while get update data")
    }

    private suspend fun getUpdatesFromApi(versionCode: Int): UpdateData {
        val args = formBodyOf(
            "query" to "app_update",
            "current" to versionCode.toString()
        )
        return updaterApi.proxy().checkUpdate(args)
            .handleApiResponse()
            .toDomain()
    }

    private suspend fun getUpdatesFromReserve(): UpdateData? {
        val singleSources = reserveSources.sources.map { source ->
            runCatching { getReserve(source) }
        }
        return singleSources
            .mapNotNull { it.getOrNull() }
            .firstOrNull()
    }

    private suspend fun getReserve(url: String): UpdateData =
        updaterApi.direct().checkReserve(url).toDomain()
}