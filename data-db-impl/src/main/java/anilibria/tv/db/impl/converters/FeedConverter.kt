package anilibria.tv.db.impl.converters

import anilibria.tv.db.impl.entity.feed.FeedDb
import anilibria.tv.domain.entity.relative.FeedRelative
import toothpick.InjectConstructor

@InjectConstructor
class FeedConverter {

    fun toDbKey(releaseId: Int?, youtubeId: Int?): String = "${releaseId}_$youtubeId"

    fun toDomain(source: FeedDb) = FeedRelative(
        releaseId = source.releaseId,
        youtubeId = source.youtubeId
    )

    fun toDb(source: FeedRelative) = FeedDb(
        key = toDbKey(source.releaseId, source.youtubeId),
        releaseId = source.releaseId,
        youtubeId = source.youtubeId
    )

    fun toDbKey(ids: List<Pair<Int?, Int?>>) = ids.map { toDbKey(it.first, it.second) }

    fun toDomain(source: List<FeedDb>) = source.map { toDomain(it) }

    fun toDb(source: List<FeedRelative>) = source.map { toDb(it) }

}