package tv.anilibria.feature.player.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import toothpick.InjectConstructor
import tv.anilibria.feature.content.types.release.EpisodeId
import tv.anilibria.feature.content.types.release.ReleaseId
import tv.anilibria.feature.player.data.domain.EpisodeVisit
import tv.anilibria.feature.player.data.local.EpisodeHistoryLocalDataSource

@InjectConstructor
class EpisodeHistoryRepository(
    private val historyStorage: EpisodeHistoryLocalDataSource
) {

    fun observeAll(): Flow<List<EpisodeVisit>> {
        return historyStorage.observe()
    }

    suspend fun getAll(): List<EpisodeVisit> {
        return historyStorage.get()
    }

    fun observeByRelease(releaseId: ReleaseId): Flow<List<EpisodeVisit>> {
        return historyStorage.observe().map { visits ->
            visits.filter { it.id.releaseId == releaseId }
        }
    }

    suspend fun getByRelease(releaseId: ReleaseId): List<EpisodeVisit> {
        return historyStorage.get().filter { it.id.releaseId == releaseId }
    }

    suspend fun markViewed(id: EpisodeId) {
        val currentVisit = getVisit(id)
        historyStorage.put(EpisodeVisit(id, currentVisit?.playerSeek, Clock.System.now()))
    }

    suspend fun markUnViewed(id: EpisodeId) {
        val currentVisit = getVisit(id)
        historyStorage.put(EpisodeVisit(id, currentVisit?.playerSeek, null))
    }

    suspend fun resetByRelease(releaseId: ReleaseId) {
        historyStorage.removeByRelease(releaseId)
    }

    suspend fun viewAllInRelease(ids: List<EpisodeId>) {
        val now = Clock.System.now()
        val newVisits = ids.map { id ->
            getVisit(id)?.copy(lastOpenAt = now) ?: EpisodeVisit(id, null, now)
        }
        historyStorage.put(newVisits)
    }

    suspend fun saveSeek(id: EpisodeId, seek: Long) {
        historyStorage.put(EpisodeVisit(id, seek, Clock.System.now()))
    }

    suspend fun remove(id: EpisodeId) {
        historyStorage.remove(id)
    }

    private suspend fun getVisit(id: EpisodeId) = historyStorage.get().find { it.id == id }
}