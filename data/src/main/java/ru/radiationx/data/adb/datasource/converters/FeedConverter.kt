package ru.radiationx.data.adb.datasource.converters

import ru.radiationx.data.adb.feed.FeedDb
import ru.radiationx.data.adb.feed.FlatFeedDb
import ru.radiationx.data.adomain.feed.FeedItem
import toothpick.InjectConstructor

@InjectConstructor
class FeedConverter(
    private val releaseConverter: ReleaseConverter,
    private val youtubeConverter: YoutubeConverter
) {

    fun toDomain(feedDb: FeedDb) = FeedItem(
        release = feedDb.release?.let { releaseConverter.toDomain(it) },
        youtube = feedDb.youtube?.let { youtubeConverter.toDomain(it) }
    )

    fun toDb(feedItem: FeedItem) = FeedDb(
        feed = toDb(feedItem.release?.id, feedItem.youtube?.id),
        release = feedItem.release?.let { releaseConverter.toDb(it) },
        youtube = feedItem.youtube?.let { youtubeConverter.toDb(it) }
    )

    fun toDb(releaseId: Int?, youtubeId: Int?) = FlatFeedDb(
        releaseId = releaseId,
        youtubeId = youtubeId
    )


    fun toDomain(items: List<FeedDb>) = items.map { toDomain(it) }

    fun toDb(items: List<FeedItem>) = items.map { toDb(it) }

}