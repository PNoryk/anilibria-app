package ru.radiationx.data.adomain.entity.schedule

import ru.radiationx.data.adomain.entity.release.Release


data class ScheduleDay(
    val day: Int,
    val items: List<Release>
)