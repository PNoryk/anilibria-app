package anilibria.tv.db.impl.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Single
import anilibria.tv.db.impl.entity.torrent.TorrentDb

@Dao
interface TorrentDao {

    @Query("SELECT * FROM release_torrent")
    fun getListAll(): Single<List<TorrentDb>>

    @Query("SELECT * FROM release_torrent WHERE releaseId IN (:releaseIds)")
    fun getList(releaseIds: List<Int>): Single<List<TorrentDb>>

    @Query("SELECT * FROM release_torrent WHERE `key` IN (:keys)")
    fun getListByKeys(keys: List<String>): Single<List<TorrentDb>>

    @Query("SELECT * FROM release_torrent WHERE releaseId = :releaseId and id = :torrentId LIMIT 1")
    fun getOne(releaseId: Int, torrentId: Int): Single<TorrentDb>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items: List<TorrentDb>): Completable

    @Query("DELETE FROM release_torrent WHERE `key` IN (:keys)")
    fun delete(keys: List<String>): Completable

    @Query("DELETE FROM release_torrent")
    fun deleteAll(): Completable
}