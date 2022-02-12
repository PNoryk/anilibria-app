package tv.anilibria.module.data.restapi.entity.mapper

import kotlinx.datetime.Instant
import tv.anilibria.core.types.asCount
import tv.anilibria.core.types.asHtmlText
import tv.anilibria.core.types.asRelativeUrl
import tv.anilibria.module.data.restapi.entity.app.youtube.YoutubeResponse
import tv.anilibria.module.domain.entity.youtube.Youtube
import tv.anilibria.module.domain.entity.youtube.YoutubeId
import tv.anilibria.module.domain.entity.youtube.YoutubeVideoId

fun YoutubeResponse.toDomain() = Youtube(
    id = YoutubeId(id),
    title = title?.asHtmlText(),
    image = image?.asRelativeUrl(),
    vid = vid?.let { YoutubeVideoId(it) },
    views = views.asCount(),
    comments = comments.asCount(),
    timestamp = Instant.fromEpochSeconds(timestamp)
)