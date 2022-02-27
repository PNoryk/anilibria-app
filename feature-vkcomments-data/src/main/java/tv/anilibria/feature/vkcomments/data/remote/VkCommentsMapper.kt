package tv.anilibria.feature.vkcomments.data.remote

import tv.anilibria.core.types.asBaseUrl
import tv.anilibria.module.domain.entity.page.VkComments

fun VkCommentsResponse.toDomain() = VkComments(
    baseUrl = baseUrl.asBaseUrl(),
    script = script
)