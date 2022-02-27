package tv.anilibria.feature.menu.data.remote

import tv.anilibria.core.types.asAbsoluteUrl
import tv.anilibria.core.types.asRelativeUrl
import tv.anilibria.module.data.restapi.entity.mapper.toDataIcon
import tv.anilibria.module.domain.entity.other.LinkMenuItem

fun LinkMenuItemResponse.toDomain() = LinkMenuItem(
    title = title,
    absoluteLink = absoluteLink?.asAbsoluteUrl(),
    sitePagePath = sitePagePath?.asRelativeUrl(),
    icon = icon?.toDataIcon()
)