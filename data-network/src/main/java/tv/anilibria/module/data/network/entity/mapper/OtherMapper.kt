package tv.anilibria.module.data.network.entity.mapper

import kotlinx.datetime.DayOfWeek
import tv.anilibria.module.data.network.entity.app.other.LinkMenuItemResponse
import tv.anilibria.module.data.network.entity.app.page.PageLibriaResponse
import tv.anilibria.module.data.network.entity.app.page.VkCommentsResponse
import tv.anilibria.module.domain.entity.common.asAbsoluteUrl
import tv.anilibria.module.domain.entity.common.asBaseUrl
import tv.anilibria.module.domain.entity.common.asHtmlText
import tv.anilibria.module.domain.entity.common.asRelativeUrl
import tv.anilibria.module.domain.entity.other.LinkMenuItem
import tv.anilibria.module.domain.entity.page.PageLibria
import tv.anilibria.module.domain.entity.page.VkComments

fun LinkMenuItemResponse.toDomain() = LinkMenuItem(
    title = title,
    absoluteLink = absoluteLink?.asAbsoluteUrl(),
    sitePagePath = sitePagePath?.asRelativeUrl(),
    icon = icon
)

fun PageLibriaResponse.toDomain() = PageLibria(
    title = title,
    content = content.asHtmlText()
)

fun VkCommentsResponse.toDomain() = VkComments(
    baseUrl = baseUrl.asBaseUrl(),
    script = script
)

fun String.asWeekDay(): DayOfWeek {
    val intDay = requireNotNull(toIntOrNull()) {
        "Day is not integer '$this'"
    }
    return DayOfWeek(intDay)
}