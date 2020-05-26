package ru.radiationx.data.adb.schedule

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import ru.radiationx.data.adb.release.FlatReleaseDb

@Entity(
    tableName = "schedule_release_cross_ref",
    primaryKeys = ["scheduleDay", "releaseId"],
    foreignKeys = [
        ForeignKey(entity = FlatScheduleDayDb::class, parentColumns = ["scheduleDayId"], childColumns = ["scheduleDayId"]),
        ForeignKey(entity = FlatReleaseDb::class, parentColumns = ["releaseId"], childColumns = ["releaseId"])
    ]
)
data class ScheduleReleaseCrossRefDb(
    @ColumnInfo(name = "scheduleDayId") val scheduleDayId: Int,
    @ColumnInfo(name = "releaseId") val releaseId: Int
)