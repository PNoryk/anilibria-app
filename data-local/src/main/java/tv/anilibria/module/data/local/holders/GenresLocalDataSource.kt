package tv.anilibria.module.data.local.holders

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tv.anilibria.plugin.data.storage.DataStorage
import tv.anilibria.plugin.data.storage.MoshiStorageDataHolder
import tv.anilibria.plugin.data.storage.ObservableData
import tv.anilibria.plugin.data.storage.storageStringKey

class GenresLocalDataSource(
    private val storage: DataStorage,
    private val moshi: Moshi
) {

    private val adapter by lazy {
        val type = Types.newParameterizedType(List::class.java, String::class.java)
        moshi.adapter<List<String>>(type)
    }

    private val persistableData = MoshiStorageDataHolder<List<String>, List<String>>(
        key = storageStringKey("refactor.genres"),
        adapter = adapter,
        storage = storage,
        read = { it },
        write = { it }
    )

    private val observableData = ObservableData(persistableData)

    fun observe(): Flow<List<String>> = observableData
        .observe()
        .map { it.orEmpty() }

    suspend fun get(): List<String> = observableData.get().orEmpty()

    suspend fun put(data: List<String>) = observableData.put(data)
}