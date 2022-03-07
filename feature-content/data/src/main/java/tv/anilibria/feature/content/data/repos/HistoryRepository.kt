package tv.anilibria.feature.content.data.repos

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock
import tv.anilibria.feature.content.data.local.holders.ReleaseHistoryLocalDataSource
import tv.anilibria.feature.content.types.ReleaseVisit
import tv.anilibria.feature.content.types.release.ReleaseId
import javax.inject.Inject

/**
 * Created by radiationx on 18.02.18.
 */
class HistoryRepository @Inject constructor(
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