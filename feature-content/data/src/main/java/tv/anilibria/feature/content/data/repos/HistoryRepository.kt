package tv.anilibria.feature.content.data.repos

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock
import toothpick.InjectConstructor
import tv.anilibria.feature.content.data.local.holders.ReleaseHistoryLocalDataSource
import tv.anilibria.feature.content.types.ReleaseVisit
import tv.anilibria.feature.content.types.release.ReleaseId

@InjectConstructor
class HistoryRepository(
    private val historyStorage: ReleaseHistoryLocalDataSource
) {

    fun observeReleases(): Flow<List<ReleaseVisit>> {
        return historyStorage.observe()
    }

    suspend fun getReleases(): List<ReleaseVisit> {
        return historyStorage.get()
    }

    suspend fun putRelease(id: ReleaseId) {
        historyStorage.put(ReleaseVisit(id, Clock.System.now()))
    }

    suspend fun removeRelease(id: ReleaseId) {
        historyStorage.remove(id)
    }
}