package tv.anilibria.plugin.data.storage

interface DataHolder<T> {
    suspend fun get(): DataWrapper<T>
    suspend fun save(data: DataWrapper<T>)
}