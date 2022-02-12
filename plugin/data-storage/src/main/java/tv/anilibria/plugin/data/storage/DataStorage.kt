package tv.anilibria.plugin.data.storage

interface DataStorage {
    suspend fun getString(key: String): DataWrapper<String>
    suspend fun setString(key: String, value: DataWrapper<String>)
    suspend fun remove(key: String)
    suspend fun clear()
}