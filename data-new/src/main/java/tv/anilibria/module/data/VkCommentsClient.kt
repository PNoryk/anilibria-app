package tv.anilibria.module.data

// todo implement
interface VkCommentsClient {

    suspend fun getBody(url: String): String
}