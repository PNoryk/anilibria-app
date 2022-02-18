package tv.anilibria.module.data

import android.util.Log
import kotlinx.coroutines.flow.*
import tv.anilibria.module.data.repos.ReleaseRepository
import tv.anilibria.module.domain.entity.Page
import tv.anilibria.module.domain.entity.release.RandomRelease
import tv.anilibria.module.domain.entity.release.Release
import tv.anilibria.module.domain.entity.release.ReleaseCode
import tv.anilibria.module.domain.entity.release.ReleaseId
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

/**
 * Created by radiationx on 17.02.18.
 */
@Deprecated("old data")
class ReleaseInteractor @Inject constructor(
    private val releaseRepository: ReleaseRepository,
) {


    private val fullLoadCacheById = ConcurrentHashMap<ReleaseId, Flow<Release>>()
    private val fullLoadCacheByCode = ConcurrentHashMap<ReleaseCode, Flow<Release>>()

    private val releaseItemsById = mutableMapOf<ReleaseId, Release>()
    private val releaseItemsByCode = mutableMapOf<ReleaseCode, Release>()

    private val releasesById = mutableMapOf<ReleaseId, Release>()
    private val releasesByCode = mutableMapOf<ReleaseCode, Release>()

    private val itemsUpdateTrigger = MutableSharedFlow<Boolean>()
    private val fullUpdateTrigger = MutableSharedFlow<Boolean>()

    suspend fun getRandomRelease(): RandomRelease = releaseRepository.getRandomRelease()

    private suspend fun loadRelease(releaseId: ReleaseId): Flow<Release> = flow<Release> {
        releaseRepository.getRelease(releaseId)
    }.onEach {
        updateFullCache(it)
    }.onEach {
        fullLoadCacheById.remove(releaseId)
    }.also {
        fullLoadCacheById[releaseId] = it
    }

    private suspend fun loadRelease(releaseCode: ReleaseCode): Flow<Release> = flow<Release> {
        releaseRepository.getRelease(releaseCode)
    }.onEach {
        updateFullCache(it)
    }.onEach {
        fullLoadCacheByCode.remove(releaseCode)
    }.also {
        fullLoadCacheByCode[releaseCode] = it
    }

    suspend fun loadRelease(
        releaseId: ReleaseId? = null,
        releaseCode: ReleaseCode? = null
    ): Flow<Release> {
        val releaseSource = releaseId
            ?.let { fullLoadCacheById[it] }
            ?: releaseCode?.let { fullLoadCacheByCode[it] }
        (releaseSource)?.also {
            return it
        }
        return when {
            releaseId != null -> loadRelease(releaseId)
            releaseCode != null -> loadRelease(releaseCode)
            else -> flow { throw Exception("Unknown id=$releaseId or code=$releaseCode") }
        }
    }

    suspend fun loadReleases(page: Int): Page<Release> = releaseRepository
        .getReleases(page)
        .also { updateItemsCache(it.items) }

    fun getItem(releaseId: ReleaseId? = null, releaseCode: ReleaseCode? = null): Release? {
        return releaseId
            ?.let { releaseItemsById[it] }
            ?: releaseCode?.let { releaseItemsByCode[it] }
    }

    fun getFull(releaseId: ReleaseId? = null, releaseCode: ReleaseCode? = null): Release? {
        return releaseId
            ?.let { releasesById[it] }
            ?: releaseCode?.let { releasesByCode[it] }
    }

    fun observeItem(releaseId: ReleaseId? = null, releaseCode: ReleaseCode? = null): Flow<Release> =
        itemsUpdateTrigger
            .onStart { emit(true) }
            .filter { getItem(releaseId, releaseCode) != null }
            .map { getItem(releaseId, releaseCode)!! }

    fun observeFull(releaseId: ReleaseId? = null, releaseCode: ReleaseCode? = null): Flow<Release> =
        itemsUpdateTrigger
            .onStart { emit(true) }
            .filter { getFull(releaseId, releaseCode) != null }
            .map { getFull(releaseId, releaseCode)!! }


    suspend fun updateItemsCache(items: List<Release>) {
        Log.e("kekeke", "updateItemsCache ${items.size}")
        items.forEach { release ->
            releaseItemsById[release.id] = release
            release.code?.also { code ->
                releaseItemsByCode[code] = release
            }
        }
        itemsUpdateTrigger.emit(true)
    }

    suspend fun updateFullCache(release: Release) {
        releasesById[release.id] = release
        release.code?.also { code ->
            releasesByCode[code] = release
        }
        fullUpdateTrigger.emit(true)
    }
}