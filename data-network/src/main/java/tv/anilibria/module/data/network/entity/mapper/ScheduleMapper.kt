package tv.anilibria.module.data.network.entity.mapper

import tv.anilibria.module.data.network.entity.app.schedule.ScheduleDayResponse
import tv.anilibria.module.domain.entity.schedule.ScheduleDay

fun ScheduleDayResponse.toDomain() = ScheduleDay(
    day = day.asWeekDay(),
    items = items.map { it.toDomain() }
)