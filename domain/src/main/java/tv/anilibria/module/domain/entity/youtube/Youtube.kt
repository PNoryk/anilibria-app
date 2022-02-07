package tv.anilibria.module.domain.entity.youtube

import kotlinx.datetime.Instant
import tv.anilibria.module.domain.entity.common.RelativeUrl
import tv.anilibria.module.domain.entity.common.HtmlText

data class Youtube(
    val id: Int,
    val title: HtmlText?,
    val image: RelativeUrl?,
    val vid: String?,
    val views: Long,
    val comments: Long,
    val timestamp: Instant
) 