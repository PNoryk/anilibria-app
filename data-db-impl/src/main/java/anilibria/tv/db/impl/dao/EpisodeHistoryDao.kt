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
    fun getListAll(): Single<List<EpisodeHistoryDb>>

    @Query("SELECT * FROM episode_history WHERE releaseId IN (:releaseIds)")
    fun getList(releaseIds: List<Int>): Single<List<EpisodeHistoryDb>>

    @Query("SELECT * FROM episode_history WHERE `key` IN (:keys)")
    fun getListByKeys(keys: List<String>): Single<List<EpisodeHistoryDb>>

    @Query("SELECT * FROM episode_history WHERE releaseId = :releaseId and id = :episodeId LIMIT 1")
    fun getOne(releaseId: Int, episodeId: Int): Single<EpisodeHistoryDb>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items: List<EpisodeHistoryDb>): Completable

    @Query("DELETE FROM episode_history WHERE `key` IN (:keys)")
    fun delete(keys: List<String>): Completable

    @Query("DELETE FROM episode_history")
    fun deleteAll(): Completable
}