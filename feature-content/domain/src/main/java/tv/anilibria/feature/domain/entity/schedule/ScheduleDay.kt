package tv.anilibria.feature.domain.entity.schedule

import kotlinx.datetime.DayOfWeek
import tv.anilibria.feature.domain.entity.release.Release

data class ScheduleDay(
    val day: DayOfWeek,
    val items: List<Release>
)