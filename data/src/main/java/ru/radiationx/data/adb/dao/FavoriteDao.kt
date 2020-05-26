package ru.radiationx.data.adb.dao

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single
import ru.radiationx.data.adb.favorite.FavoriteDb
import ru.radiationx.data.adb.favorite.FlatFavoriteDb
import ru.radiationx.data.adb.torrent.TorrentDb

@Dao
interface FavoriteDao {

    @Transaction
    @Query("SELECT * FROM `favorite`")
    fun getList(): Single<List<FavoriteDb>>

    @Transaction
    @Query("SELECT * FROM `favorite` WHERE releaseId = :releaseId")
    fun getOne(releaseId: Int): Single<FavoriteDb>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSome(items: List<FlatFavoriteDb>): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOne(item: FlatFavoriteDb): Completable

    @Delete
    fun deleteSome(items: List<FlatFavoriteDb>): Completable

    @Delete
    fun deleteOne(item: FlatFavoriteDb): Completable
}