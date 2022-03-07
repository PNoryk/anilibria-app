package tv.anilibria.feature.content.data.remote.entity.mapper

import tv.anilibria.feature.content.data.remote.entity.app.schedule.ScheduleDayResponse
import tv.anilibria.feature.content.types.schedule.ScheduleDay

fun ScheduleDayResponse.toDomain() = ScheduleDay(
    day = day.asWeekDay(),
    items = items.map { it.toDomain() }
)