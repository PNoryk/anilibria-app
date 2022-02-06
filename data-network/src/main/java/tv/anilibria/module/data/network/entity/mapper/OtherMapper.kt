package tv.anilibria.module.data.network.entity.mapper

import tv.anilibria.module.data.network.entity.app.other.LinkMenuItemResponse
import tv.anilibria.module.data.network.entity.app.page.PageLibriaResponse
import tv.anilibria.module.data.network.entity.app.page.VkCommentsResponse
import tv.anilibria.module.domain.entity.other.LinkMenuItem
import tv.anilibria.module.domain.entity.page.PageLibria
import tv.anilibria.module.domain.entity.page.VkComments

fun LinkMenuItemResponse.toDomain() = LinkMenuItem(
    title = title,
    absoluteLink = absoluteLink,
    sitePagePath = sitePagePath,
    icon = icon
)

fun PageLibriaResponse.toDomain() = PageLibria(
    title = title,
    content = content
)

fun VkCommentsResponse.toDomain() = VkComments(
    baseUrl = baseUrl,
    script = script
)