package tv.anilibria.plugin.data.storage

class SimpleStorageDataKeyHolder<T>(
    private val key: StorageKey<T>,
    private val storage: DataKeyStorage
) : DataHolder<T> {

    override suspend fun get(): T? {
        return storage.get(key)
    }

    override suspend fun save(data: T?) {
        storage.set(key, data)
    }
}