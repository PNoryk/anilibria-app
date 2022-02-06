package tv.anilibria.module.domain.entity.schedule

import tv.anilibria.module.domain.entity.release.Release

data class ScheduleDay(
    val day: String,
    val items: List<Release>
)