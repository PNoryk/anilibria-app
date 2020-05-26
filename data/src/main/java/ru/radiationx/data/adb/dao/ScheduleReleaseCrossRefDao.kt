package ru.radiationx.data.adb.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Transaction
import io.reactivex.Completable
import ru.radiationx.data.adb.schedule.ScheduleReleaseCrossRefDb

@Dao
interface ScheduleReleaseCrossRefDao {

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(crossRef: ScheduleReleaseCrossRefDb): Completable
}