package tv.anilibria.feature.vkcomments.data

import tv.anilibria.core.types.AbsoluteUrl

interface VkCommentsClient {

    suspend fun getBody(url: AbsoluteUrl): String
}