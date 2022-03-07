package tv.anilibria.feature.menu.data.domain

import tv.anilibria.core.types.AbsoluteUrl
import tv.anilibria.core.types.RelativeUrl
import tv.anilibria.feature.domain.entity.other.DataIcon

data class LinkMenuItem(
    val title: String,
    val absoluteLink: AbsoluteUrl?,
    val sitePagePath: RelativeUrl?,
    val icon: DataIcon?
)