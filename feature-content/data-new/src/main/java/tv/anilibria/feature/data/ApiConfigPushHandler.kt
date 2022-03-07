package tv.anilibria.feature.data

interface ApiConfigPushHandler {
    suspend fun handlePushData(rawData: String)
}