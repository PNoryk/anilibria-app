package anilibria.tv.db.impl.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import anilibria.tv.db.impl.entity.episode.EpisodeDb
import io.reactivex.Completable
import io.reactivex.Single
import anilibria.tv.db.impl.entity.torrent.TorrentDb

@Dao
interface TorrentDao {

    @Query("SELECT * FROM release_torrent")
    fun getList(): Single<List<TorrentDb>>

    @Query("SELECT * FROM release_torrent WHERE `key` IN (:keys)")
    fun getSome(keys: List<String>): Single<List<TorrentDb>>

    @Query("SELECT * FROM release_torrent WHERE releaseId IN (:releaseIds)")
    fun getSomeByReleases(releaseIds: List<Int>): Single<List<TorrentDb>>

    @Query("SELECT * FROM release_torrent WHERE `key` = :key LIMIT 1")
    fun getOne(key: String): Single<TorrentDb>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items: List<TorrentDb>): Completable

    @Query("DELETE FROM release_torrent WHERE `key` IN (:keys)")
    fun remove(keys: List<String>): Completable

    @Query("DELETE FROM release_torrent")
    fun clear(): Completable
}