package tv.anilibria.feature.content.data.repos

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import toothpick.InjectConstructor
import tv.anilibria.feature.content.data.local.holders.ReleaseCacheLocalDataSource
import tv.anilibria.feature.content.types.release.Release
import tv.anilibria.feature.content.types.release.ReleaseCode
import tv.anilibria.feature.content.types.release.ReleaseId

@InjectConstructor
class ReleaseCacheRepository(
    private val releaseCache: ReleaseCacheLocalDataSource
) {

    fun observeRelease(
        releaseId: ReleaseId?,
        releaseCode: ReleaseCode?
    ): Flow<Release?> {
        return releaseCache.observe().map { it.findRelease(releaseId, releaseCode) }
    }

    suspend fun awaitRelease(
        releaseId: ReleaseId?,
        releaseCode: ReleaseCode?
    ): Release {
        return observeRelease(releaseId, releaseCode).filterNotNull().first()
    }

    suspend fun getRelease(
        releaseId: ReleaseId?,
        releaseCode: ReleaseCode?
    ): Release? {
        return releaseCache.get().findRelease(releaseId, releaseCode)
    }

    private fun Map<ReleaseId, Release>.findRelease(
        releaseId: ReleaseId?,
        releaseCode: ReleaseCode?
    ): Release? {
        return get(releaseId) ?: values.find { it.id == releaseId || it.code == releaseCode }
    }
}