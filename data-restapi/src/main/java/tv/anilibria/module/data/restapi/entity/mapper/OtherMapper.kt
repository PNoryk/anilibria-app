package tv.anilibria.module.data.restapi.entity.mapper

import kotlinx.datetime.DayOfWeek
import tv.anilibria.module.data.restapi.entity.app.other.LinkMenuItemResponse
import tv.anilibria.module.data.restapi.entity.app.page.PageLibriaResponse
import tv.anilibria.module.data.restapi.entity.app.page.VkCommentsResponse
import tv.anilibria.module.domain.entity.common.asAbsoluteUrl
import tv.anilibria.module.domain.entity.common.asBaseUrl
import tv.anilibria.module.domain.entity.common.asHtmlText
import tv.anilibria.module.domain.entity.common.asRelativeUrl
import tv.anilibria.module.domain.entity.other.DataColor
import tv.anilibria.module.domain.entity.other.DataIcon
import tv.anilibria.module.domain.entity.other.LinkMenuItem
import tv.anilibria.module.domain.entity.page.PageLibria
import tv.anilibria.module.domain.entity.page.VkComments

fun LinkMenuItemResponse.toDomain() = LinkMenuItem(
    title = title,
    absoluteLink = absoluteLink?.asAbsoluteUrl(),
    sitePagePath = sitePagePath?.asRelativeUrl(),
    icon = icon?.toDataIcon()
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

fun String.toDataIcon(): DataIcon = when (this) {
    "vk" -> DataIcon.VK
    "youtube",
    "yotube" -> DataIcon.YOUTUBE
    "patreon" -> DataIcon.PATREON
    "telegram" -> DataIcon.TELEGRAM
    "discord" -> DataIcon.DISCORD
    "yoomoney" -> DataIcon.YOOMONEY
    "donationalerts" -> DataIcon.DONATIONALERTS
    "anilibria" -> DataIcon.ANILIBRIA
    "info" -> DataIcon.INFO
    "rules" -> DataIcon.RULES
    "person" -> DataIcon.PERSON
    "site" -> DataIcon.SITE
    "infra" -> DataIcon.INFRA
    else -> DataIcon.UNKNOWN
}

fun String.toDataColor(): DataColor = when (this) {
    "vk" -> DataColor.VK
    "youtube",
    "yotube" -> DataColor.YOUTUBE
    "patreon" -> DataColor.PATREON
    "telegram" -> DataColor.TELEGRAM
    "discord" -> DataColor.DISCORD
    "yoomoney" -> DataColor.YOOMONEY
    "donationalerts" -> DataColor.DONATIONALERTS
    "anilibria" -> DataColor.ANILIBRIA
    else -> DataColor.UNKNOWN
}