package ru.radiationx.data.adb.dao

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single
import ru.radiationx.data.adb.entity.history.HistoryDb

@Dao
interface HistoryDao {

    @Query("SELECT * FROM `history`")
    fun getList(): Single<List<HistoryDb>>

    @Query("SELECT * FROM `history` WHERE releaseId = :releaseId")
    fun getOne(releaseId: Int): Single<HistoryDb>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items: List<HistoryDb>): Completable

    @Query("DELETE FROM history")
    fun deleteAll(): Completable
}