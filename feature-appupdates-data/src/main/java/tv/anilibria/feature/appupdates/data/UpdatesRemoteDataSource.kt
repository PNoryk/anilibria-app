package tv.anilibria.feature.appupdates.data

import toothpick.InjectConstructor
import tv.anilibria.feature.appupdates.data.domain.UpdateData
import tv.anilibria.plugin.data.network.BaseUrlsProvider
import tv.anilibria.plugin.data.network.formBodyOf
import tv.anilibria.plugin.data.restapi.handleApiResponse

@InjectConstructor
class UpdatesRemoteDataSource(
    private val updaterApi: UpdaterApiWrapper,
    private val reserveSources: CheckerReserveSources,
    private val urlsProvider: BaseUrlsProvider
) {

    suspend fun checkUpdate(versionCode: Int): UpdateData {
        return runCatching { getUpdatesFromApi(versionCode) }
            .onFailure { it.printStackTrace() }
            .getOrNull()
            ?: getUpdatesFromReserve()
            ?: throw IllegalStateException("Error while get update data")
    }

    private suspend fun getUpdatesFromApi(versionCode: Int): UpdateData {
        val args = formBodyOf(
            "query" to "app_update",
            "current" to versionCode.toString()
        )
        return updaterApi.proxy()
            .checkUpdate(urlsProvider.api.value, args)
            .handleApiResponse()
            .toDomain()
    }

    private suspend fun getUpdatesFromReserve(): UpdateData? {
        val singleSources = reserveSources.sources.map { source ->
            runCatching {
                getReserve(source)
            }.onFailure {
                it.printStackTrace()
            }
        }
        return singleSources
            .mapNotNull { it.getOrNull() }
            .firstOrNull()
    }

    private suspend fun getReserve(url: String): UpdateData =
        updaterApi.direct().checkReserve(url).toDomain()
}