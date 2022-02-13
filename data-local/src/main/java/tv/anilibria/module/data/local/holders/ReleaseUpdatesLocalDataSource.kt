package tv.anilibria.module.data.local.holders

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tv.anilibria.module.data.local.entity.ReleaseUpdateLocal
import tv.anilibria.module.data.local.mappers.toDomain
import tv.anilibria.module.data.local.mappers.toLocal
import tv.anilibria.module.domain.entity.ReleaseUpdate
import tv.anilibria.module.domain.entity.release.ReleaseId
import tv.anilibria.plugin.data.storage.DataStorage
import tv.anilibria.plugin.data.storage.MoshiStorageDataHolder
import tv.anilibria.plugin.data.storage.ObservableData

class ReleaseUpdatesLocalDataSource(
    private val storage: DataStorage,
    private val moshi: Moshi
) {

    private val adapter by lazy {
        val type = Types.newParameterizedType(List::class.java, ReleaseUpdateLocal::class.java)
        moshi.adapter<List<ReleaseUpdateLocal>>(type)
    }

    private val persistableData =
        MoshiStorageDataHolder<List<ReleaseUpdateLocal>, List<ReleaseUpdate>>(
            key = "refactor.release_updates",
            adapter = adapter,
            storage = storage,
            read = { data -> data?.map { it.toDomain() } },
            write = { data -> data?.map { it.toLocal() } }
        )

    private val observableData = ObservableData(persistableData)

    fun observe(): Flow<List<ReleaseUpdate>> = observableData
        .observe()
        .map { it.orEmpty() }

    suspend fun get(): List<ReleaseUpdate> = observableData
        .get()
        .orEmpty()

    suspend fun put(data: List<ReleaseUpdate>) = observableData.update { currentData ->
        currentData?.toMutableList()?.apply {
            val dataIds = data.map { it.id }.toSet()
            removeAll { dataIds.contains(it.id) }
            addAll(data)
        }
    }

    suspend fun remove(releaseId: ReleaseId) = observableData.update { currentData ->
        currentData?.filter { it.id == releaseId }
    }
}