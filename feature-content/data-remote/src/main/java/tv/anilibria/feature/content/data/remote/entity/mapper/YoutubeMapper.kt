package tv.anilibria.feature.content.data.remote.entity.mapper

import kotlinx.datetime.Instant
import tv.anilibria.core.types.asCount
import tv.anilibria.core.types.asHtmlText
import tv.anilibria.core.types.asRelativeUrl
import tv.anilibria.feature.content.data.remote.entity.app.youtube.YoutubeResponse
import tv.anilibria.feature.content.types.youtube.Youtube
import tv.anilibria.feature.content.types.youtube.YoutubeId
import tv.anilibria.feature.content.types.youtube.YoutubeVideoId

fun YoutubeResponse.toDomain() = Youtube(
    id = YoutubeId(id),
    title = title?.asHtmlText(),
    image = image?.asRelativeUrl(),
    vid = vid?.let { YoutubeVideoId(it) },
    views = views.asCount(),
    comments = comments.asCount(),
    timestamp = Instant.fromEpochSeconds(timestamp)
)