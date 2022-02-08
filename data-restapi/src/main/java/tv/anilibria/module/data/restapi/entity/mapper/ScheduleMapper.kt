package tv.anilibria.module.data.restapi.entity.mapper

import tv.anilibria.module.data.restapi.entity.app.schedule.ScheduleDayResponse
import tv.anilibria.module.domain.entity.schedule.ScheduleDay

fun ScheduleDayResponse.toDomain() = ScheduleDay(
    day = day.asWeekDay(),
    items = items.map { it.toDomain() }
)