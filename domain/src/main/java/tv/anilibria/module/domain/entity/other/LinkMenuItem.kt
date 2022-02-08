package tv.anilibria.module.domain.entity.other

import tv.anilibria.core.types.AbsoluteUrl
import tv.anilibria.core.types.RelativeUrl

data class LinkMenuItem(
    val title: String,
    val absoluteLink: AbsoluteUrl?,
    val sitePagePath: RelativeUrl?,
    val icon: DataIcon?
)