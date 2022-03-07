package tv.anilibria.feature.data.restapi.entity.mapper

import tv.anilibria.feature.data.restapi.entity.app.feed.FeedResponse
import tv.anilibria.feature.domain.entity.feed.Feed

fun FeedResponse.toDomain() = Feed(
    release?.toDomain(),
    youtube?.toDomain()
)