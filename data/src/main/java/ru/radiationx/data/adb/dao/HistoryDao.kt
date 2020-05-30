package ru.radiationx.data.adb.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Single
import ru.radiationx.data.adb.entity.history.HistoryDb

@Dao
interface HistoryDao {

    @Query("SELECT * FROM `history`")
    fun getList(): Single<List<HistoryDb>>

    @Query("SELECT * FROM history WHERE releaseId IN (:ids)")
    fun getList(ids: List<Int>): Single<List<HistoryDb>>

    @Query("SELECT * FROM `history` WHERE releaseId = :releaseId LIMIT 1")
    fun getOne(releaseId: Int): Single<HistoryDb>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items: List<HistoryDb>): Completable

    @Query("DELETE FROM history WHERE releaseId IN (:ids)")
    fun delete(ids: List<Int>): Completable

    @Query("DELETE FROM history")
    fun deleteAll(): Completable
}