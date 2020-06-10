package anilibria.tv.db.impl.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Single
import anilibria.tv.db.impl.entity.episode.EpisodeDb

@Dao
interface EpisodeDao {

    @Query("SELECT * FROM release_episode")
    fun getListAll(): Single<List<EpisodeDb>>

    @Query("SELECT * FROM release_episode WHERE `key` IN (:keys)")
    fun getSome(keys: List<String>): Single<List<EpisodeDb>>

    @Query("SELECT * FROM release_episode WHERE releaseId IN (:releaseIds)")
    fun getSomeByReleases(releaseIds: List<Int>): Single<List<EpisodeDb>>

    @Query("SELECT * FROM release_episode WHERE `key` = :key LIMIT 1")
    fun getOne(key: String): Single<EpisodeDb>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items: List<EpisodeDb>): Completable

    @Query("DELETE FROM release_episode WHERE `key` IN (:keys)")
    fun remove(keys: List<String>): Completable

    @Query("DELETE FROM release_episode")
    fun clear(): Completable
}