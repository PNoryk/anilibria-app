package ru.radiationx.data.adomain


data class ScheduleDay(
    val day: Int,
    val items: List<Release>
)