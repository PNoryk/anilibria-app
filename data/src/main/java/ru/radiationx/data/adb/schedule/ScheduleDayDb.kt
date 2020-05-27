package ru.radiationx.data.adb.schedule

import androidx.room.Embedded
import androidx.room.Relation

data class ScheduleDayDb(
    @Embedded val scheduleDay: FlatScheduleDayDb,
    @Relation(
        parentColumn = "scheduleDayId",
        entityColumn = "releaseId"
    )
    val items: List<ScheduleReleaseDb>
)