package anilibria.tv.db.impl.converters

import anilibria.tv.db.impl.entity.history.ReleaseHistoryDb
import anilibria.tv.domain.entity.relative.ReleaseHistoryRelative
import toothpick.InjectConstructor

@InjectConstructor
class ReleaseHistoryConverter {

    fun toDomain(source: ReleaseHistoryDb) = ReleaseHistoryRelative(
        releaseId = source.releaseId,
        timestamp = source.timestamp
    )

    fun toDb(source: ReleaseHistoryRelative) = ReleaseHistoryDb(
        releaseId = source.releaseId,
        timestamp = source.timestamp
    )

    fun toDomain(source: List<ReleaseHistoryDb>) = source.map { toDomain(it) }

    fun toDb(source: List<ReleaseHistoryRelative>) = source.map { toDb(it) }
}