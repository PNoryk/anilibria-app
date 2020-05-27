package ru.radiationx.data.adb.dao

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single
import ru.radiationx.data.adb.feed.FeedDb

@Dao
interface FeedDao {

    @Query("SELECT * FROM `feed`")
    fun getList(): Single<List<FeedDb>>

    @Query("SELECT * FROM `feed` WHERE id = :feedId")
    fun getOne(feedId: Int): Single<FeedDb>

    @Query("SELECT * FROM `feed` WHERE releaseId = :releaseId OR youtubeId = :youtubeId")
    fun getOne(releaseId: Int?, youtubeId: Int?): Single<FeedDb>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items: List<FeedDb>): Completable

    @Query("DELETE FROM feed")
    fun deleteAll(): Completable
}