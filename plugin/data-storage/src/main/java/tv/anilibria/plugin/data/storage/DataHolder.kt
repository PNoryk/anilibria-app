package tv.anilibria.plugin.data.storage

interface DataHolder<T> {
    suspend fun get(): T?
    suspend fun save(data: T?)
}