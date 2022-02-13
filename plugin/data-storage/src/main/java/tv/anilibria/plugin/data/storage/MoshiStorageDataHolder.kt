package tv.anilibria.plugin.data.storage

import com.squareup.moshi.JsonAdapter

class MoshiStorageDataHolder<M, T>(
    private val key: StorageKey.StringKey,
    private val adapter: JsonAdapter<M>,
    private val storage: DataStorage,
    private val read: (M?) -> T?,
    private val write: (T?) -> M?
) : DataHolder<T?> {

    override suspend fun get(): T? {
        return storage.get(key).let { jsonString ->
            val jsonData = jsonString?.let { adapter.fromJson(it) }
            read.invoke(jsonData)
        }
    }

    override suspend fun save(data: T?) {
        val jsonString = write.invoke(data)?.let { jsonData -> adapter.toJson(jsonData) }
        storage.set(key, jsonString)
    }
}