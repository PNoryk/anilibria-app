package tv.anilibria.feature.vkcomments.data.remote

import kotlinx.coroutines.withTimeout
import tv.anilibria.core.types.AbsoluteUrl
import tv.anilibria.feature.vkcomments.data.domain.VkComments
import tv.anilibria.plugin.data.network.ApiWrapper
import tv.anilibria.plugin.data.network.formBodyOf
import tv.anilibria.plugin.data.restapi.handleApiResponse
import java.net.UnknownHostException
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

/**
 * Created by radiationx on 13.01.18.
 */
// todo проверить, что при ошибке body не возвращается
class VkCommentsRemoteDataSource @Inject constructor(
    private val commentsApi: ApiWrapper<VkCommentsApi>
) {

    suspend fun getComments(): VkComments {
        val args = formBodyOf(
            "query" to "vkcomments"
        )
        return commentsApi.proxy()
            .getVkComments(args)
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