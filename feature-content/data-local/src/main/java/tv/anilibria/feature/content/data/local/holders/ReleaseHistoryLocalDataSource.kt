package tv.anilibria.feature.content.data.local.holders

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tv.anilibria.feature.content.data.local.di.ReleaseHistoryStorageQualifier
import tv.anilibria.feature.content.data.local.entity.ReleaseVisitLocal
import tv.anilibria.feature.content.data.local.mappers.toDomain
import tv.anilibria.feature.content.data.local.mappers.toLocal
import tv.anilibria.feature.content.types.ReleaseVisit
import tv.anilibria.feature.content.types.release.ReleaseId
import tv.anilibria.plugin.data.storage.DataStorage
import tv.anilibria.plugin.data.storage.MoshiStorageDataHolder
import tv.anilibria.plugin.data.storage.ObservableData
import tv.anilibria.plugin.data.storage.storageStringKey

class ReleaseHistoryLocalDataSource(
    @ReleaseHistoryStorageQualifier private val storage: DataStorage,
    private val moshi: Moshi
) {

    private val adapter by lazy {
        val type = Types.newParameterizedType(List::class.java, ReleaseVisitLocal::class.java)
        moshi.adapter<List<ReleaseVisitLocal>>(type)
    }

    private val persistableData =
        MoshiStorageDataHolder<List<ReleaseVisitLocal>, List<ReleaseVisit>>(
            key = storageStringKey("refactor.release_history"),
            adapter = adapter,
            storage = storage,
            read = { data -> data?.map { it.toDomain() } },
            write = { data -> data?.map { it.toLocal() } }
        )

    private val observableData = ObservableData(persistableData)

    fun observe(): Flow<List<ReleaseVisit>> = observableData
        .observe()
        .map { it.orEmpty() }

    suspend fun get(): List<ReleaseVisit> = observableData
        .get()
        .orEmpty()

    suspend fun put(data: ReleaseVisit) = observableData.update { currentData ->
        currentData?.toMutableList()?.apply {
            removeAll { it.id == data.id }
            add(data)
        }
    }

    suspend fun remove(releaseId: ReleaseId) = observableData.update { currentData ->
        currentData?.filter { it.id == releaseId }
    }
}