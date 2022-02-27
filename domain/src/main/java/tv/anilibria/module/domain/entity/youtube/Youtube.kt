package tv.anilibria.module.domain.entity.youtube

import kotlinx.datetime.Instant
import tv.anilibria.core.types.AbsoluteUrl
import tv.anilibria.core.types.Count
import tv.anilibria.core.types.HtmlText
import tv.anilibria.core.types.RelativeUrl

data class Youtube(
    val id: YoutubeId,
    val title: HtmlText?,
    val image: RelativeUrl?,
    val vid: YoutubeVideoId?,
    val views: Count,
    val comments: Count,
    val timestamp: Instant
) {

    val link: AbsoluteUrl? = vid?.let {
        AbsoluteUrl("https://www.youtube.com/watch?v=${it.id}")
    }
}