package tv.anilibria.feature.data.restapi.entity.mapper

import tv.anilibria.feature.data.restapi.entity.app.schedule.ScheduleDayResponse
import tv.anilibria.feature.domain.entity.schedule.ScheduleDay

fun ScheduleDayResponse.toDomain() = ScheduleDay(
    day = day.asWeekDay(),
    items = items.map { it.toDomain() }
)