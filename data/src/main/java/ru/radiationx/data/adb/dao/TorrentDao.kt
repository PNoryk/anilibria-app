package ru.radiationx.data.adb.dao

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single
import ru.radiationx.data.adb.entity.torrent.TorrentDb

@Dao
interface TorrentDao {

    @Query("SELECT * FROM release_torrent")
    fun getListAll(): Single<List<TorrentDb>>

    @Query("SELECT * FROM release_torrent WHERE releaseId = :releaseId")
    fun getList(releaseId: Int): Single<List<TorrentDb>>

    @Query("SELECT * FROM release_torrent WHERE releaseId = :releaseId and id = :torrentId")
    fun getOne(releaseId: Int, torrentId: Int): Single<TorrentDb>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items: List<TorrentDb>): Completable

    @Query("DELETE FROM release_torrent")
    fun deleteAll(): Completable
}