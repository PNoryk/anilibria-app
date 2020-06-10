package anilibria.tv.db.impl.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import anilibria.tv.db.impl.entity.episode.EpisodeDb
import anilibria.tv.db.impl.entity.history.EpisodeHistoryDb
import io.reactivex.Completable
import io.reactivex.Single
import anilibria.tv.db.impl.entity.history.ReleaseHistoryDb

@Dao
interface EpisodeHistoryDao {

    @Query("SELECT * FROM episode_history")
    fun getList(): Single<List<EpisodeHistoryDb>>

    @Query("SELECT * FROM episode_history WHERE `key` IN (:keys)")
    fun getSome(keys: List<String>): Single<List<EpisodeHistoryDb>>

    @Query("SELECT * FROM episode_history WHERE releaseId IN (:releaseIds)")
    fun getSomeByReleases(releaseIds: List<Int>): Single<List<EpisodeHistoryDb>>

    @Query("SELECT * FROM episode_history WHERE `key` = :key LIMIT 1")
    fun getOne(key: String): Single<EpisodeHistoryDb>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items: List<EpisodeHistoryDb>): Completable

    @Query("DELETE FROM episode_history WHERE `key` IN (:keys)")
    fun remove(keys: List<String>): Completable

    @Query("DELETE FROM episode_history")
    fun clear(): Completable
}