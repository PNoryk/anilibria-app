package anilibria.tv.api.impl.converter

import anilibria.tv.domain.entity.feed.Feed
import anilibria.tv.api.impl.entity.feed.FeedItemResponse
import toothpick.InjectConstructor

@InjectConstructor
class FeedConverter(
    private val releaseConverter: ReleaseConverter,
    private val youtubeConverter: YoutubeConverter
) {

    fun toDomain(response: FeedItemResponse) = Feed(
        release = response.release?.let { releaseConverter.toDomain(it) },
        youtube = response.youtube?.let { youtubeConverter.toDomain(it) }
    )
}