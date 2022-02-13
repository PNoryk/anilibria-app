package tv.anilibria.plugin.data.storage

class StorageDataHolder<T>(
    private val key: String,
    private val storage: DataStorage,
    private val read: (String?) -> T?,
    private val write: (T?) -> String?
) : DataHolder<T> {

    override suspend fun get(): T? {
        return read.invoke(storage.getString(key))
    }

    override suspend fun save(data: T?) {
        storage.setString(key, write.invoke(data))
    }
}