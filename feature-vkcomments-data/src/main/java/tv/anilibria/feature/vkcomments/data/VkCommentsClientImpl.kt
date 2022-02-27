package tv.anilibria.feature.vkcomments.data

import tv.anilibria.core.types.AbsoluteUrl
import tv.anilibria.feature.vkcomments.data.VkCommentsClient
import tv.anilibria.feature.vkcomments.data.VkCommentsRepository

class VkCommentsClientImpl(
    private val vkCommentsRepository: VkCommentsRepository
) : VkCommentsClient {

    override suspend fun getBody(url: AbsoluteUrl): String {
        return vkCommentsRepository.getDirectBody(url)
    }
}