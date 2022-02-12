package tv.anilibria.module.data.local.holders

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tv.anilibria.module.data.local.entity.EpisodeVisitLocal
import tv.anilibria.module.data.local.mappers.toDomain
import tv.anilibria.module.data.local.mappers.toLocal
import tv.anilibria.module.domain.entity.EpisodeVisit
import tv.anilibria.module.domain.entity.release.EpisodeId
import tv.anilibria.module.domain.entity.release.ReleaseId
import tv.anilibria.plugin.data.storage.DataStorage
import tv.anilibria.plugin.data.storage.DataWrapper
import tv.anilibria.plugin.data.storage.MoshiStorageDataHolder
import tv.anilibria.plugin.data.storage.ObservableData

class EpisodeHistoryLocalDataSource(
    private val storage: DataStorage,
    private val moshi: Moshi
) {

    private val adapter by lazy {
        val type = Types.newParameterizedType(List::class.java, EpisodeVisitLocal::class.java)
        moshi.adapter<List<EpisodeVisitLocal>>(type)
    }

    private val persistableData =
        MoshiStorageDataHolder<List<EpisodeVisitLocal>, List<EpisodeVisit>>(
            key = "refactor.episode_history",
            adapter = adapter,
            storage = storage,
            read = { data -> data?.map { it.toDomain() } },
            write = { data -> data?.map { it.toLocal() } }
        )

    private val observableData = ObservableData(persistableData)

    fun observe(): Flow<List<EpisodeVisit>> = observableData
        .observe()
        .map { it.data.orEmpty() }

    suspend fun get(): List<EpisodeVisit> = observableData
        .get()
        .data.orEmpty()

    suspend fun put(data: EpisodeVisit) = observableData.update { currentData ->
        val items = currentData.data?.toMutableList()?.apply {
            removeAll { it.id == data.id }
            add(data)
        }
        DataWrapper(items)
    }

    suspend fun remove(episodeId: EpisodeId) = observableData.update { currentData ->
        val items = currentData.data?.filter { it.id == episodeId }
        DataWrapper(items)
    }

    suspend fun removeByRelease(releaseId: ReleaseId) = observableData.update { currentData ->
        val items = currentData.data?.filter { it.id.releaseId == releaseId }
        DataWrapper(items)
    }
}