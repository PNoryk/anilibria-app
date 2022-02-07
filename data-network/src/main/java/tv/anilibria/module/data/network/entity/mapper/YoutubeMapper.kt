package tv.anilibria.module.data.network.entity.mapper

import kotlinx.datetime.Instant
import tv.anilibria.module.data.network.entity.app.youtube.YoutubeResponse
import tv.anilibria.module.domain.entity.common.asHtmlText
import tv.anilibria.module.domain.entity.common.asRelativeUrl
import tv.anilibria.module.domain.entity.youtube.Youtube

fun YoutubeResponse.toDomain() = Youtube(
    id = id,
    title = title?.asHtmlText(),
    image = image?.asRelativeUrl(),
    vid = vid,
    views = views,
    comments = comments,
    timestamp = Instant.fromEpochSeconds(timestamp)
)