package tv.anilibria.feature.content.types.schedule

import kotlinx.datetime.DayOfWeek
import tv.anilibria.feature.content.types.release.Release

data class ScheduleDay(
    val day: DayOfWeek,
    val items: List<Release>
)