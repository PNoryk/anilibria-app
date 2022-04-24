package tv.anilibria.feature.content.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import toothpick.InjectConstructor
import tv.anilibria.feature.content.data.repos.ReleaseRepository
import tv.anilibria.feature.content.types.release.RandomRelease
import tv.anilibria.feature.content.types.release.Release
import tv.anilibria.feature.content.types.release.ReleaseCode
import tv.anilibria.feature.content.types.release.ReleaseId
import tv.anilibria.plugin.data.storage.InMemoryDataHolder
import tv.anilibria.plugin.data.storage.ObservableData

@InjectConstructor
class ReleaseInteractor(
    private val releaseRepository: ReleaseRepository,
) {

    private val cache = ObservableData<Map<ReleaseId, Release>>(InMemoryDataHolder(emptyMap()))

    suspend fun getRandomRelease(): RandomRelease = releaseRepository.getRandomRelease()

    fun observeRelease(
        releaseId: ReleaseId?,
        releaseCode: ReleaseCode?
    ): Flow<Release?> {
        return cache.observe().map { it.findRelease(releaseId, releaseCode) }
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
        return cache.get().findRelease(releaseId, releaseCode)
    }

    suspend fun fetchRelease(
        releaseId: ReleaseId?,
        releaseCode: ReleaseCode?
    ): Release {
        val release = when {
            releaseId != null -> releaseRepository.getRelease(releaseId)
            releaseCode != null -> releaseRepository.getRelease(releaseCode)
            else -> throw Exception("Unknown id=$releaseId or code=$releaseCode")
        }
        cache.update { currentData ->
            currentData.plus(release.id to release)
        }
        return release
    }

    private fun Map<ReleaseId, Release>.findRelease(
        releaseId: ReleaseId?,
        releaseCode: ReleaseCode?
    ): Release? {
        return get(releaseId) ?: values.find { it.id == releaseId || it.code == releaseCode }
    }


}