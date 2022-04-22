package tv.anilibria.feature.player.data.local

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import toothpick.InjectConstructor
import tv.anilibria.feature.player.data.domain.EpisodeVisit
import tv.anilibria.feature.content.types.release.EpisodeId
import tv.anilibria.feature.content.types.release.ReleaseId
import tv.anilibria.feature.player.data.di.EpisodeHistoryStorageQualifier
import tv.anilibria.plugin.data.storage.DataStorage
import tv.anilibria.plugin.data.storage.MoshiStorageDataHolder
import tv.anilibria.plugin.data.storage.ObservableData
import tv.anilibria.plugin.data.storage.storageStringKey

@InjectConstructor
class EpisodeHistoryLocalDataSource(
    @EpisodeHistoryStorageQualifier private val storage: DataStorage,
    private val moshi: Moshi
) {

    private val adapter by lazy {
        val type = Types.newParameterizedType(List::class.java, EpisodeVisitLocal::class.java)
        moshi.adapter<List<EpisodeVisitLocal>>(type)
    }

    private val persistableData =
        MoshiStorageDataHolder<List<EpisodeVisitLocal>, List<EpisodeVisit>>(
            key = storageStringKey("refactor.episode_history"),
            adapter = adapter,
            storage = storage,
            read = { data -> data?.map { it.toDomain() } },
            write = { data -> data?.map { it.toLocal() } }
        )

    private val observableData = ObservableData(persistableData)

    fun observe(): Flow<List<EpisodeVisit>> = observableData
        .observe()
        .map { it.orEmpty() }

    suspend fun get(): List<EpisodeVisit> = observableData
        .get()
        .orEmpty()

    suspend fun put(data: EpisodeVisit) = observableData.update { currentData ->
        currentData?.toMutableList()?.apply {
            removeAll { it.id == data.id }
            add(data)
        }
    }

    suspend fun put(data: List<EpisodeVisit>) = observableData.update { currentData ->
        currentData?.toMutableList()?.apply {
            val newIds = data.map { it.id }
            removeAll { newIds.contains(it.id) }
            addAll(data)
        }
    }

    suspend fun remove(episodeId: EpisodeId) = observableData.update { currentData ->
        currentData?.filter { it.id == episodeId }
    }

    suspend fun removeByRelease(releaseId: ReleaseId) = observableData.update { currentData ->
        currentData?.filter { it.id.releaseId == releaseId }

    }
}