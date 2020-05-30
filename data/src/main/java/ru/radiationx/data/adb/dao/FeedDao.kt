package ru.radiationx.data.adb.dao

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single
import ru.radiationx.data.adb.entity.favorite.FavoriteDb
import ru.radiationx.data.adb.entity.feed.FeedDb

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