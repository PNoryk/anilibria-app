package tv.anilibria.feature.content.data.local.holders

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tv.anilibria.feature.domain.entity.ReleaseYear
import tv.anilibria.plugin.data.storage.DataStorage
import tv.anilibria.plugin.data.storage.MoshiStorageDataHolder
import tv.anilibria.plugin.data.storage.ObservableData
import tv.anilibria.plugin.data.storage.storageStringKey

class YearsLocalDataSource(
    private val storage: DataStorage,
    private val moshi: Moshi
) {

    private val adapter by lazy {
        val type = Types.newParameterizedType(List::class.java, String::class.java)
        moshi.adapter<List<String>>(type)
    }

    private val persistableData = MoshiStorageDataHolder<List<String>, List<ReleaseYear>>(
        key = storageStringKey("refactor.years"),
        adapter = adapter,
        storage = storage,
        read = { data -> data?.map { ReleaseYear(it) } },
        write = { data -> data?.map { it.value } }
    )

    private val observableData = ObservableData(persistableData)

    fun observe(): Flow<List<ReleaseYear>> = observableData
        .observe()
        .map { it.orEmpty() }

    suspend fun get(): List<ReleaseYear> = observableData
        .get()
        .orEmpty()

    suspend fun put(data: List<ReleaseYear>) = observableData.put(data)
}