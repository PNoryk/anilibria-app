package tv.anilibria.feature.downloads.data

import kotlinx.coroutines.runBlocking
import toothpick.InjectConstructor
import tv.anilibria.plugin.data.storage.DataStorage
import tv.anilibria.plugin.data.storage.ObservableData
import tv.anilibria.plugin.data.storage.StorageDataHolder

@InjectConstructor
class DownloadsStorage(
    private val storage: DataStorage
) {

    companion object {
        private const val KEY_DOWNLOADS = "data.download_ids"
    }

    private val dataHolder = StorageDataHolder(
        key = KEY_DOWNLOADS,
        storage = storage,
        read = { string ->
            string?.split(",")
                ?.filter { it.isNotEmpty() }
                ?.map { it.toLong() }
        },
        write = { items ->
            items?.joinToString(",")
        }
    )

    private val observableData = ObservableData(dataHolder)

    fun getDownloads(): List<Long> {
        return runBlocking { observableData.get().orEmpty() }
    }

    fun saveDownloads(items: List<Long>) {
        runBlocking { observableData.put(items) }
    }
}