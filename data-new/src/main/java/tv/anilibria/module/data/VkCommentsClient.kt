package tv.anilibria.module.data

interface VkCommentsClient {

    suspend fun getBody(url: String): String
}