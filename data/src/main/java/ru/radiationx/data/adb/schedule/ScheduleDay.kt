package ru.radiationx.data.adb.schedule

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import ru.radiationx.data.adb.release.ReleaseDb
import ru.radiationx.data.adb.schedule.FlatScheduleDayDb

data class ScheduleDay(
    @Embedded val scheduleDay: FlatScheduleDayDb,
    @Relation(
        parentColumn = "scheduleDayId",
        entityColumn = "releaseId",
        associateBy = Junction(ScheduleReleaseCrossRefDb::class)
    )
    val items: List<ReleaseDb>
)