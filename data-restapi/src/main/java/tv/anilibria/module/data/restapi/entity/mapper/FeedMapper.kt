package tv.anilibria.module.data.restapi.entity.mapper

import tv.anilibria.module.data.restapi.entity.app.feed.FeedResponse
import tv.anilibria.module.domain.entity.feed.Feed

fun FeedResponse.toDomain() = Feed(
    release?.toDomain(),
    youtube?.toDomain()
)