package ru.radiationx.data.adb.schedule

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schedule_day")
data class FlatScheduleDayDb(
    @PrimaryKey @ColumnInfo(name = "scheduleDayId") val dayId: Int
)