package ru.radiationx.data.adb.datasource.converters

import ru.radiationx.data.adb.schedule.FlatScheduleDayDb
import ru.radiationx.data.adb.schedule.ScheduleDayDb
import ru.radiationx.data.adomain.schedule.ScheduleDay
import toothpick.InjectConstructor

@InjectConstructor
class ScheduleConverter(
    private val releaseConverter: ReleaseConverter
) {

    fun toDomain(scheduleDayDb: ScheduleDayDb) = ScheduleDay(
        day = scheduleDayDb.scheduleDay.dayId,
        items = scheduleDayDb.items.map(releaseConverter::toDomain)
    )

    fun toDb(scheduleDay: ScheduleDay) = ScheduleDayDb(
        scheduleDay = toDb(scheduleDay.day),
        items = scheduleDay.items.map(releaseConverter::toDb)
    )

    fun toDb(dayId: Int) = FlatScheduleDayDb(dayId)


    fun toDomain(items: List<ScheduleDayDb>) = items.map { toDomain(it) }

    fun toDb(items: List<ScheduleDay>) = items.map { toDb(it) }
}