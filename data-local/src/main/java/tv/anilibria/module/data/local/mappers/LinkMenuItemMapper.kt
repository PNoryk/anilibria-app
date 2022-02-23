package tv.anilibria.module.data.local.mappers

import tv.anilibria.core.types.asAbsoluteUrl
import tv.anilibria.core.types.asRelativeUrl
import tv.anilibria.module.data.local.entity.LinkMenuItemLocal
import tv.anilibria.module.domain.entity.other.LinkMenuItem

fun LinkMenuItem.toLocal() = LinkMenuItemLocal(
    title = title,
    absoluteLink = absoluteLink?.value,
    sitePagePath = sitePagePath?.value,
    icon = icon?.toLocal()
)

fun LinkMenuItemLocal.toDomain() = LinkMenuItem(
    title = title,
    absoluteLink = absoluteLink?.asAbsoluteUrl(),
    sitePagePath = sitePagePath?.asRelativeUrl(),
    icon = icon?.toDataIcon()
)