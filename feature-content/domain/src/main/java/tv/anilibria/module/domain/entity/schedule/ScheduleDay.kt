package tv.anilibria.module.domain.entity.schedule

import kotlinx.datetime.DayOfWeek
import tv.anilibria.module.domain.entity.release.Release

data class ScheduleDay(
    val day: DayOfWeek,
    val items: List<Release>
)