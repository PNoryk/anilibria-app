package tv.anilibria.module.domain.entity.youtube

import kotlinx.datetime.Instant
import tv.anilibria.module.domain.entity.common.Count
import tv.anilibria.module.domain.entity.common.HtmlText
import tv.anilibria.module.domain.entity.common.RelativeUrl

data class YoutubeId(val id: Long)

data class YoutubeVideoId(val id: String)

data class Youtube(
    val id: YoutubeId,
    val title: HtmlText?,
    val image: RelativeUrl?,
    val vid: YoutubeVideoId?,
    val views: Count,
    val comments: Count,
    val timestamp: Instant
) 