package tv.anilibria.plugin.data.storage

interface DataStorage {
    suspend fun getString(key: String): String?
    suspend fun setString(key: String, value: String?)
    suspend fun remove(key: String)
    suspend fun clear()
}