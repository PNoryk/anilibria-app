package tv.anilibria.feature.content.data.local.holders

import kotlinx.coroutines.flow.Flow
import toothpick.InjectConstructor
import tv.anilibria.feature.content.types.release.Release
import tv.anilibria.feature.content.types.release.ReleaseId
import tv.anilibria.plugin.data.storage.InMemoryDataHolder
import tv.anilibria.plugin.data.storage.ObservableData

@InjectConstructor
class ReleaseCacheLocalDataSource {

    private val cache = ObservableData<Map<ReleaseId, Release>>(InMemoryDataHolder(emptyMap()))

    fun observe(): Flow<Map<ReleaseId, Release>> = cache.observe()

    suspend fun get(): Map<ReleaseId, Release> = cache.get()

    suspend fun put(data: List<Release>) = cache.update { currentData ->
        currentData.plus(data.associateBy { it.id })
    }
}