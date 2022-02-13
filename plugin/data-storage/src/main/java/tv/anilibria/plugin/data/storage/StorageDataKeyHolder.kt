package tv.anilibria.plugin.data.storage

class StorageDataKeyHolder<S, T>(
    private val key: StorageKey<S>,
    private val storage: DataKeyStorage,
    private val read: (S?) -> T?,
    private val write: (T?) -> S?
) : DataHolder<T> {

    override suspend fun get(): T? {
        return read.invoke(storage.get(key))
    }

    override suspend fun save(data: T?) {
        storage.set(key, write.invoke(data))
    }
}