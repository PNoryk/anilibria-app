package tv.anilibria.feature.content.data.restapi.entity.mapper

import tv.anilibria.feature.content.data.restapi.entity.app.feed.FeedResponse
import tv.anilibria.feature.domain.entity.feed.Feed

fun FeedResponse.toDomain() = Feed(
    release?.toDomain(),
    youtube?.toDomain()
)