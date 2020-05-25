package ru.radiationx.data.api.converter

import ru.radiationx.data.adomain.FeedItem
import ru.radiationx.data.api.remote.FeedItemResponse
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