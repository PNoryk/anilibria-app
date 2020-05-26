package ru.radiationx.data.adb.dao

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single
import ru.radiationx.data.adb.favorite.FavoriteDb
import ru.radiationx.data.adb.favorite.FlatFavoriteDb
import ru.radiationx.data.adb.history.FlatHistoryDb
import ru.radiationx.data.adb.history.HistoryDb
import ru.radiationx.data.adb.torrent.TorrentDb

@Dao
interface HistoryDao {

    @Transaction
    @Query("SELECT * FROM `history`")
    fun getList(): Single<List<HistoryDb>>

    @Transaction
    @Query("SELECT * FROM `history` WHERE releaseId = :releaseId")
    fun getOne(releaseId: Int): Single<HistoryDb>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items: List<FlatHistoryDb>): Completable

    @Delete
    fun delete(items: List<FlatHistoryDb>): Completable
}