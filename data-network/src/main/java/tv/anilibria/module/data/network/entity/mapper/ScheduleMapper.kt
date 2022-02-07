package tv.anilibria.module.data.network.entity.mapper

import kotlinx.datetime.DayOfWeek
import tv.anilibria.module.data.network.entity.app.schedule.ScheduleDayResponse
import tv.anilibria.module.domain.entity.schedule.ScheduleDay

fun ScheduleDayResponse.toDomain() = ScheduleDay(
    day = day.toReleaseDay(),
    items = items.map { it.toDomain() }
)

fun String.toReleaseDay(): DayOfWeek {
    val intDay = requireNotNull(toIntOrNull()) {
        "Day is not integer '$this'"
    }
    return DayOfWeek(intDay)
}
