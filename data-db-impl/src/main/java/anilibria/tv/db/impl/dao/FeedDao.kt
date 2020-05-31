package anilibria.tv.db.impl.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Single
import anilibria.tv.db.impl.entity.feed.FeedDb

@Dao
interface FeedDao {

    @Query("SELECT * FROM `feed`")
    fun getList(): Single<List<FeedDb>>

    @Query("SELECT * FROM feed WHERE `key` IN (:keys)")
    fun getList(keys: List<String>): Single<List<FeedDb>>

    @Query("SELECT * FROM `feed` WHERE releaseId = :releaseId OR youtubeId = :youtubeId LIMIT 1")
    fun getOne(releaseId: Int?, youtubeId: Int?): Single<FeedDb>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items: List<FeedDb>): Completable

    @Query("DELETE FROM feed WHERE `key` IN (:keys)")
    fun delete(keys: List<String>): Completable

    @Query("DELETE FROM feed")
    fun deleteAll(): Completable
}