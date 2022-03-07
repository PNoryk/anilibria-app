package tv.anilibria.feature.menu.data.remote

import tv.anilibria.core.types.asAbsoluteUrl
import tv.anilibria.core.types.asRelativeUrl
import tv.anilibria.feature.content.data.restapi.entity.mapper.toDataIcon
import tv.anilibria.feature.menu.data.domain.LinkMenuItem

fun LinkMenuItemResponse.toDomain() = LinkMenuItem(
    title = title,
    absoluteLink = absoluteLink?.asAbsoluteUrl(),
    sitePagePath = sitePagePath?.asRelativeUrl(),
    icon = icon?.toDataIcon()
)