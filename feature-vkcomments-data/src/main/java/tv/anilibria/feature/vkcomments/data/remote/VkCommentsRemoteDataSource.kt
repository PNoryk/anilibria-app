package tv.anilibria.feature.vkcomments.data.remote

import kotlinx.coroutines.withTimeout
import toothpick.InjectConstructor
import tv.anilibria.core.types.AbsoluteUrl
import tv.anilibria.feature.vkcomments.data.domain.VkComments
import tv.anilibria.plugin.data.network.BaseUrlsProvider
import tv.anilibria.plugin.data.network.formBodyOf
import tv.anilibria.plugin.data.restapi.handleApiResponse
import java.net.UnknownHostException
import kotlin.time.Duration.Companion.seconds

// todo проверить, что при ошибке body не возвращается
@InjectConstructor
class VkCommentsRemoteDataSource(
    private val commentsApi: VkCommentsApiWrapper,
    private val urlsProvider: BaseUrlsProvider
) {

    suspend fun getComments(): VkComments {
        val args = formBodyOf(
            "query" to "vkcomments"
        )
        return commentsApi.proxy()
            .getVkComments(urlsProvider.api.value, args)
            .handleApiResponse()
            .toDomain()
    }

    suspend fun checkVkBlocked(): Boolean = try {
        withTimeout(15L.seconds) {
            commentsApi.direct().checkVkBlocked("https://vk.com/")
        }
        false
    } catch (ex: Exception) {
        ex !is UnknownHostException
    }

    suspend fun getDirectBody(url: AbsoluteUrl): String {
        return commentsApi.direct().getBody(url.value).string()
    }
}