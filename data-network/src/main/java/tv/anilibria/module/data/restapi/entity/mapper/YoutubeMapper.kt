package tv.anilibria.module.data.restapi.entity.mapper

import kotlinx.datetime.Instant
import tv.anilibria.module.data.restapi.entity.app.youtube.YoutubeResponse
import tv.anilibria.module.domain.entity.common.asCount
import tv.anilibria.module.domain.entity.common.asHtmlText
import tv.anilibria.module.domain.entity.common.asRelativeUrl
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