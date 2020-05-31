package anilibria.tv.db.impl.converters

import anilibria.tv.db.impl.entity.schedule.FlatScheduleDayDb
import anilibria.tv.db.impl.entity.schedule.ScheduleDayDb
import anilibria.tv.db.impl.entity.schedule.ScheduleReleaseDb
import anilibria.tv.domain.entity.relative.ScheduleDayRelative
import toothpick.InjectConstructor

@InjectConstructor
class ScheduleConverter {

    fun toDomain(source: ScheduleDayDb) = ScheduleDayRelative(
        dayId = source.scheduleDay.dayId,
        releaseIds = source.items.map { it.releaseId }
    )

    fun toDb(source: ScheduleDayRelative) = ScheduleDayDb(
        scheduleDay = FlatScheduleDayDb(source.dayId),
        items = source.releaseIds.map { releaseId ->
            ScheduleReleaseDb(source.dayId, releaseId)
        }
    )

    fun toDomain(source: List<ScheduleDayDb>) = source.map { toDomain(it) }

    fun toDb(source: List<ScheduleDayRelative>) = source.map { toDb(it) }
}