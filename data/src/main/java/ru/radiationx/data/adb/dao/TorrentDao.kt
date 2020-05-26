package ru.radiationx.data.adb.dao

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single
import ru.radiationx.data.adb.torrent.TorrentDb

@Dao
interface TorrentDao {

    @Query("SELECT * FROM release_torrent")
    fun getListAll(): Single<List<TorrentDb>>

    @Query("SELECT * FROM release_torrent WHERE releaseId = :releaseId")
    fun getList(releaseId: Int): Single<List<TorrentDb>>

    @Query("SELECT * FROM release_torrent WHERE releaseId = :releaseId and id = :torrentId")
    fun getOne(releaseId: Int, torrentId: Int): Single<TorrentDb>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSome(items: List<TorrentDb>): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOne(item: TorrentDb): Completable

    @Delete
    fun deleteSome(items: List<TorrentDb>): Completable

    @Delete
    fun deleteOne(item: TorrentDb): Completable
}