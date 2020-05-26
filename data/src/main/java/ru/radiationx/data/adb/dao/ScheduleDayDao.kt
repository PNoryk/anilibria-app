package ru.radiationx.data.adb.dao

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single
import ru.radiationx.data.adb.favorite.FavoriteDb
import ru.radiationx.data.adb.favorite.FlatFavoriteDb
import ru.radiationx.data.adb.history.FlatHistoryDb
import ru.radiationx.data.adb.history.HistoryDb
import ru.radiationx.data.adb.schedule.FlatScheduleDayDb
import ru.radiationx.data.adb.schedule.ScheduleDayDb
import ru.radiationx.data.adb.torrent.TorrentDb

@Dao
interface ScheduleDayDao {

    @Transaction
    @Query("SELECT * FROM `schedule_day`")
    fun getList(): Single<List<ScheduleDayDb>>

    @Transaction
    @Query("SELECT * FROM `schedule_day` WHERE scheduleDayId = :scheduleDayId")
    fun getOne(scheduleDayId: Int): Single<ScheduleDayDb>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSome(items: List<FlatScheduleDayDb>): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOne(item: FlatScheduleDayDb): Completable

    @Delete
    fun deleteSome(items: List<FlatScheduleDayDb>): Completable

    @Delete
    fun deleteOne(item: FlatScheduleDayDb): Completable
}