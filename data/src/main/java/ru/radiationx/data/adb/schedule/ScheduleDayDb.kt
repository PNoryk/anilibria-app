package ru.radiationx.data.adb.schedule

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import ru.radiationx.data.adb.release.FlatReleaseDb
import ru.radiationx.data.adb.release.ReleaseDb
import ru.radiationx.data.adb.schedule.FlatScheduleDayDb

data class ScheduleDayDb(
    @Embedded val scheduleDay: FlatScheduleDayDb,
    @Relation(
        entity = FlatReleaseDb::class,
        parentColumn = "scheduleDayId",
        entityColumn = "releaseId",
        associateBy = Junction(ScheduleReleaseCrossRefDb::class)
    )
    val items: List<ReleaseDb>
)