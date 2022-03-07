package tv.anilibria.feature.content.data.remote.entity.mapper

import tv.anilibria.feature.content.data.remote.entity.app.feed.FeedResponse
import tv.anilibria.feature.domain.entity.feed.Feed

fun FeedResponse.toDomain() = Feed(
    release?.toDomain(),
    youtube?.toDomain()
)