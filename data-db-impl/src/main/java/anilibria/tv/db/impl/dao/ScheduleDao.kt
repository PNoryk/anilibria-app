package anilibria.tv.db.impl.dao

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single
import anilibria.tv.db.impl.entity.schedule.FlatScheduleDayDb
import anilibria.tv.db.impl.entity.schedule.ScheduleDayDb
import anilibria.tv.db.impl.entity.schedule.ScheduleReleaseDb

@Dao
abstract class ScheduleDao {

    @Transaction
    @Query("SELECT * FROM `schedule_day`")
    abstract fun getListAll(): Single<List<ScheduleDayDb>>

    @Query("SELECT * FROM schedule_day WHERE scheduleDayId IN (:ids)")
    abstract fun getList(ids: List<Int>): Single<List<ScheduleDayDb>>

    @Transaction
    @Query("SELECT * FROM `schedule_day` WHERE scheduleDayId = :scheduleDayId LIMIT 1")
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

    @Query("DELETE FROM schedule_day WHERE scheduleDayId IN (:ids)")
    abstract fun delete(ids: List<Int>): Completable

    @Query("DELETE FROM schedule_day")
    abstract fun deleteAll(): Completable
}