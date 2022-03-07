package tv.anilibria.feature.content.data

interface ApiConfigPushHandler {
    suspend fun handlePushData(rawData: String)
}