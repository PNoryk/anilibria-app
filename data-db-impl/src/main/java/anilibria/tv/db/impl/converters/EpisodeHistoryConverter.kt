package anilibria.tv.db.impl.converters

import anilibria.tv.db.impl.entity.history.EpisodeHistoryDb
import anilibria.tv.domain.entity.common.keys.EpisodeKey
import anilibria.tv.domain.entity.relative.EpisodeHistoryRelative
import toothpick.InjectConstructor

@InjectConstructor
class EpisodeHistoryConverter {

    fun toDbKey(releaseId: Int, episodeId: Int?): String = "${releaseId}_$episodeId"

    fun toDbKey(key: EpisodeKey): String = toDbKey(key.releaseId, key.id)

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

    fun toDbKey(keys: List<EpisodeKey>) = keys.map { toDbKey(it) }

    fun toDomain(source: List<EpisodeHistoryDb>) = source.map { toDomain(it) }

    fun toDb(source: List<EpisodeHistoryRelative>) = source.map { toDb(it) }
}