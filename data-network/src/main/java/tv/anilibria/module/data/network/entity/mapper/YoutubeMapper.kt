package tv.anilibria.module.data.network.entity.mapper

import tv.anilibria.module.data.network.entity.app.youtube.YoutubeResponse
import tv.anilibria.module.domain.entity.youtube.Youtube

fun YoutubeResponse.toDomain() = Youtube(
    id = id,
    title = title,
    image = image,
    vid = vid,
    views = views,
    comments = comments,
    timestamp = timestamp
)