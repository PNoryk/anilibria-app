package tv.anilibria.module.domain.entity.other

import tv.anilibria.module.domain.entity.common.AbsoluteUrl
import tv.anilibria.module.domain.entity.common.RelativeUrl

data class LinkMenuItem(
    val title: String,
    val absoluteLink: AbsoluteUrl?,
    val sitePagePath: RelativeUrl?,
    val icon: LinkMenuItemIcon?
)