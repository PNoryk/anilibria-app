package tv.anilibria.feature.menu.data.local

import tv.anilibria.core.types.asAbsoluteUrl
import tv.anilibria.core.types.asRelativeUrl
import tv.anilibria.feature.content.types.other.toDataIcon
import tv.anilibria.feature.content.types.other.toKey
import tv.anilibria.feature.menu.data.domain.LinkMenuItem

fun LinkMenuItem.toLocal() = LinkMenuItemLocal(
    title = title,
    absoluteLink = absoluteLink?.value,
    sitePagePath = sitePagePath?.value,
    icon = icon?.toKey()
)

fun LinkMenuItemLocal.toDomain() = LinkMenuItem(
    title = title,
    absoluteLink = absoluteLink?.asAbsoluteUrl(),
    sitePagePath = sitePagePath?.asRelativeUrl(),
    icon = icon?.toDataIcon()
)