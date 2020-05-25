package ru.radiationx.data.adomain.schedule

import ru.radiationx.data.adomain.release.Release


data class ScheduleDay(
    val day: Int,
    val items: List<Release>
)