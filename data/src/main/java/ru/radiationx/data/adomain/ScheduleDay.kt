package ru.radiationx.data.adomain


data class ScheduleDay(
    val day: String,
    val items: List<Release>
)