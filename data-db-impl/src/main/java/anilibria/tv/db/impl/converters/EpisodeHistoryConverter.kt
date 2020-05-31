package anilibria.tv.db.impl.converters

import anilibria.tv.db.impl.entity.history.EpisodeHistoryDb
import anilibria.tv.db.impl.entity.history.ReleaseHistoryDb
import anilibria.tv.domain.entity.relative.EpisodeHistoryRelative
import anilibria.tv.domain.entity.relative.ReleaseHistoryRelative
import toothpick.InjectConstructor

@InjectConstructor
class EpisodeHistoryConverter {

    fun toDbKey(releaseId: Int, episodeId: Int): String = "${releaseId}_$episodeId"

    fun toDomain(source: EpisodeHistoryDb) = EpisodeHistoryRelative(
        releaseId = source.releaseId,
        id = source.id,
        seek = source.seek,
        lastAccess = source.lastAccess,
        isViewed = source.isViewed
    )

    fun toDb(source: EpisodeHistoryRelative) = EpisodeHistoryDb(
        key = toDbKey(source.releaseId, source.id),
        releaseId = source.releaseId,
        id = source.id,
        seek = source.seek,
        lastAccess = source.lastAccess,
        isViewed = source.isViewed
    )

    fun toDbKey(ids: List<Pair<Int, Int>>) = ids.map { toDbKey(it.first, it.second) }

    fun toDomain(source: List<EpisodeHistoryDb>) = source.map { toDomain(it) }

    fun toDb(source: List<EpisodeHistoryRelative>) = source.map { toDb(it) }
}