package tv.anilibria.feature.vkcomments.data.remote

import kotlinx.coroutines.withTimeout
import tv.anilibria.core.types.AbsoluteUrl
import tv.anilibria.module.data.restapi.datasource.remote.parsers.PagesParser
import tv.anilibria.module.domain.entity.page.VkComments
import tv.anilibria.plugin.data.network.NetworkUrlProvider
import tv.anilibria.plugin.data.network.NetworkWrapper
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
    private val pagesParser: PagesParser,
    private val urlProvider: NetworkUrlProvider,
    private val otherApi: NetworkWrapper<tv.anilibria.feature.vkcomments.data.remote.VkCommentsApi>
) {

    suspend fun getComments(): VkComments {
        val args = formBodyOf(
            "query" to "vkcomments"
        )
        return otherApi.proxy()
            .getVkComments(args)
            .handleApiResponse()
            .toDomain()
    }

    suspend fun checkVkBlocked(): Boolean = try {
        withTimeout(15L.seconds) {
            otherApi.direct().checkVkBlocked("https://vk.com/")
        }
        false
    } catch (ex: Exception) {
        ex !is UnknownHostException
    }

    suspend fun getDirectBody(url: AbsoluteUrl): String {
        return otherApi.direct().getBody(url.value).string()
    }
}