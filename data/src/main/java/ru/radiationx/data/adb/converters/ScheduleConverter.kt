package ru.radiationx.data.adb.converters

import ru.radiationx.data.adb.entity.schedule.FlatScheduleDayDb
import ru.radiationx.data.adb.entity.schedule.ScheduleDayDb
import ru.radiationx.data.adb.entity.schedule.ScheduleReleaseDb
import ru.radiationx.data.adomain.relative.ScheduleDayRelative
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