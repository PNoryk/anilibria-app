package tv.anilibria.plugin.data.storage

import kotlinx.coroutines.flow.Flow

interface DataStorage {
    fun observeAllUpdates(): Flow<String>
    suspend fun <T> get(key: StorageKey<T>): T?
    suspend fun <T> set(key: StorageKey<T>, value: T?)
    suspend fun remove(key: String)
    suspend fun clear()
}