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
    fun getSome(keys: List<String>): Single<List<FeedDb>>

    @Query("SELECT * FROM `feed` WHERE `key` = :key LIMIT 1")
    fun getOne(key: String): Single<FeedDb>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items: List<FeedDb>): Completable

    @Query("DELETE FROM feed WHERE `key` IN (:keys)")
    fun remove(keys: List<String>): Completable

    @Query("DELETE FROM feed")
    fun clear(): Completable
}