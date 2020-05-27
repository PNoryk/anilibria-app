package ru.radiationx.data.adb.entity.schedule

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "schedule_release_cross_ref",
    primaryKeys = ["scheduleDayId", "releaseId"],
    foreignKeys = [
        ForeignKey(entity = FlatScheduleDayDb::class, parentColumns = ["scheduleDayId"], childColumns = ["scheduleDayId"])
    ],
    indices = [Index("scheduleDayId"), Index("releaseId")]
)
data class ScheduleReleaseDb(
    @ColumnInfo(name = "scheduleDayId") val scheduleDayId: Int,
    @ColumnInfo(name = "releaseId") val releaseId: Int
)