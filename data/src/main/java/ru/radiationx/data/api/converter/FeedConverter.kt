package ru.radiationx.data.api.converter

import ru.radiationx.data.adomain.feed.FeedItem
import ru.radiationx.data.api.remote.feed.FeedItemResponse
import toothpick.InjectConstructor

@InjectConstructor
class FeedConverter(
    private val releaseConverter: ReleaseConverter,
    private val youtubeConverter: YoutubeConverter
) {

    fun toDomain(response: FeedItemResponse) = FeedItem(
        release = response.release?.let { releaseConverter.toDomain(it) },
        youtube = response.youtube?.let { youtubeConverter.toDomain(it) }
    )
}