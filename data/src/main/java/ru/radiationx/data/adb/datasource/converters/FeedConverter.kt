package ru.radiationx.data.adb.datasource.converters

import ru.radiationx.data.adb.feed.FeedDb
import ru.radiationx.data.adomain.relative.FeedRelative
import toothpick.InjectConstructor

@InjectConstructor
class FeedConverter {

    fun toDomain(source: FeedDb) = FeedRelative(
        releaseId = source.releaseId,
        youtubeId = source.youtubeId
    )

    fun toDb(source: FeedRelative) = FeedDb(
        source.releaseId,
        source.youtubeId
    )

    fun toDomain(source: List<FeedDb>) = source.map { toDomain(it) }

    fun toDb(source: List<FeedRelative>) = source.map { toDb(it) }

}