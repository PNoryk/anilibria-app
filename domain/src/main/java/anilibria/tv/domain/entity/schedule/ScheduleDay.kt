package anilibria.tv.domain.entity.schedule

import anilibria.tv.domain.entity.release.Release


data class ScheduleDay(
    val day: Int,
    val items: List<Release>
)