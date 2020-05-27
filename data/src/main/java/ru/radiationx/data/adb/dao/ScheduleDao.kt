package ru.radiationx.data.adb.dao

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single
import ru.radiationx.data.adb.schedule.FlatScheduleDayDb
import ru.radiationx.data.adb.schedule.ScheduleDayDb
import ru.radiationx.data.adb.schedule.ScheduleReleaseDb

@Dao
abstract class ScheduleDao {

    @Transaction
    @Query("SELECT * FROM `schedule_day`")
    abstract fun getListAll(): Single<List<ScheduleDayDb>>

    @Transaction
    @Query("SELECT * FROM `schedule_day` WHERE scheduleDayId = :scheduleDayId")
    abstract fun getOne(scheduleDayId: Int): Single<ScheduleDayDb>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items: List<ScheduleDayDb>): Completable {
        val actions = mutableListOf<Completable>()
        items.forEach { scheduleDayDb ->
            actions.add(insertReleases(scheduleDayDb.items))
            actions.add(insert(scheduleDayDb.scheduleDay))
        }
        return Completable.concatArray(*actions.toTypedArray())
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(item: FlatScheduleDayDb): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertReleases(items: List<ScheduleReleaseDb>): Completable

    @Query("DELETE FROM schedule_day")
    abstract fun deleteAll(): Completable
}