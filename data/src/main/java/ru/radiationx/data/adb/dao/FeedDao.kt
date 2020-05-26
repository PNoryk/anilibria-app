package ru.radiationx.data.adb.dao

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single
import ru.radiationx.data.adb.favorite.FavoriteDb
import ru.radiationx.data.adb.favorite.FlatFavoriteDb
import ru.radiationx.data.adb.feed.FeedDb
import ru.radiationx.data.adb.feed.FlatFeedDb
import ru.radiationx.data.adb.torrent.TorrentDb

@Dao
interface FeedDao {

    @Query("SELECT * FROM `feed`")
    fun getList(): Single<List<FeedDb>>

    @Query("SELECT * FROM `feed` WHERE id = :feedId")
    fun getOne(feedId: Int): Single<FeedDb>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSome(items: List<FlatFeedDb>): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOne(item: FlatFeedDb): Completable

    @Delete
    fun deleteSome(items: List<FlatFeedDb>): Completable

    @Delete
    fun deleteOne(item: FlatFeedDb): Completable
}