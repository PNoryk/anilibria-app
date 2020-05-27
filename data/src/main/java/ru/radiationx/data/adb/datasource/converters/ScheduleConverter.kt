package ru.radiationx.data.adb.datasource.converters

import ru.radiationx.data.adb.schedule.FlatScheduleDayDb
import ru.radiationx.data.adb.schedule.ScheduleDayDb
import ru.radiationx.data.adb.schedule.ScheduleReleaseDb
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