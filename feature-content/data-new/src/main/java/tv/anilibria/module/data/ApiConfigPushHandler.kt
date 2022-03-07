package tv.anilibria.module.data

interface ApiConfigPushHandler {
    suspend fun handlePushData(rawData: String)
}