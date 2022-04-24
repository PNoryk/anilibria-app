package tv.anilibria.feature.content.data

import android.util.Log
import kotlinx.coroutines.flow.*
import toothpick.InjectConstructor
import tv.anilibria.feature.content.data.repos.ReleaseRepository
import tv.anilibria.feature.content.types.release.RandomRelease
import tv.anilibria.feature.content.types.release.Release
import tv.anilibria.feature.content.types.release.ReleaseCode
import tv.anilibria.feature.content.types.release.ReleaseId
import tv.anilibria.plugin.data.storage.InMemoryDataHolder
import tv.anilibria.plugin.data.storage.ObservableData
import java.util.concurrent.ConcurrentHashMap

@InjectConstructor
class ReleaseInteractor(
    private val releaseRepository: ReleaseRepository,
) {

    private val cache = ObservableData<Map<ReleaseId, Release>>(InMemoryDataHolder(emptyMap()))

    private val fullLoadCacheById = ConcurrentHashMap<ReleaseId, Flow<Release>>()
    private val fullLoadCacheByCode = ConcurrentHashMap<ReleaseCode, Flow<Release>>()

    private val releaseItemsById = mutableMapOf<ReleaseId, Release>()
    private val releaseItemsByCode = mutableMapOf<ReleaseCode, Release>()

    private val releasesById = mutableMapOf<ReleaseId, Release>()
    private val releasesByCode = mutableMapOf<ReleaseCode, Release>()

    private val itemsUpdateTrigger = MutableSharedFlow<Boolean>()
    private val fullUpdateTrigger = MutableSharedFlow<Boolean>()

    suspend fun getRandomRelease(): RandomRelease = releaseRepository.getRandomRelease()

    fun observeRelease(
        releaseId: ReleaseId?,
        releaseCode: ReleaseCode?
    ): Flow<Release?> {
        return cache.observe().map { it.findRelease(releaseId, releaseCode) }
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

    /* DEPRECATED ZONE */
    private fun loadRelease(releaseId: ReleaseId): Flow<Release> = flow<Release> {
        emit(releaseRepository.getRelease(releaseId))
    }.onEach {
        updateFullCache(it)
    }.onEach {
        fullLoadCacheById.remove(releaseId)
    }.also {
        fullLoadCacheById[releaseId] = it
    }

    private fun loadRelease(releaseCode: ReleaseCode): Flow<Release> = flow<Release> {
        emit(releaseRepository.getRelease(releaseCode))
    }.onEach {
        updateFullCache(it)
    }.onEach {
        fullLoadCacheByCode.remove(releaseCode)
    }.also {
        fullLoadCacheByCode[releaseCode] = it
    }


    @Deprecated("Use new methods")
    fun loadRelease(
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

    @Deprecated("Use new methods")
    fun getItem(releaseId: ReleaseId? = null, releaseCode: ReleaseCode? = null): Release? {
        return releaseId
            ?.let { releaseItemsById[it] }
            ?: releaseCode?.let { releaseItemsByCode[it] }
    }

    @Deprecated("Use new methods")
    fun getFull(releaseId: ReleaseId? = null, releaseCode: ReleaseCode? = null): Release? {
        return releaseId
            ?.let { releasesById[it] }
            ?: releaseCode?.let { releasesByCode[it] }
    }

    @Deprecated("Use new methods")
    fun observeItem(releaseId: ReleaseId? = null, releaseCode: ReleaseCode? = null): Flow<Release> =
        itemsUpdateTrigger
            .onStart { emit(true) }
            .filter { getItem(releaseId, releaseCode) != null }
            .map { getItem(releaseId, releaseCode)!! }

    @Deprecated("Use new methods")
    fun observeFull(releaseId: ReleaseId? = null, releaseCode: ReleaseCode? = null): Flow<Release> =
        itemsUpdateTrigger
            .onStart { emit(true) }
            .filter { getFull(releaseId, releaseCode) != null }
            .map { getFull(releaseId, releaseCode)!! }


    @Deprecated("Use new methods")
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

    @Deprecated("Use new methods")
    suspend fun updateFullCache(release: Release) {
        releasesById[release.id] = release
        release.code?.also { code ->
            releasesByCode[code] = release
        }
        fullUpdateTrigger.emit(true)
    }
}