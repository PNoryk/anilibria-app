package ru.radiationx.data.adb.dao

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single
import ru.radiationx.data.adb.favorite.FavoriteDb
import ru.radiationx.data.adb.favorite.FlatFavoriteDb
import ru.radiationx.data.adb.feed.FeedDb
import ru.radiationx.data.adb.feed.FlatFeedDb
import ru.radiationx.data.adb.release.ReleaseDb
import ru.radiationx.data.adb.torrent.TorrentDb

@Dao
abstract class FeedDao {

    @Transaction
    @Query("SELECT * FROM `feed`")
    abstract fun getList(): Single<List<FeedDb>>

    @Transaction
    @Query("SELECT * FROM `feed` WHERE id = :feedId")
    abstract fun getOne(feedId: Int): Single<FeedDb>

    @Transaction
    @Query("SELECT * FROM `feed` WHERE releaseId = :releaseId OR youtubeId = :youtubeId")
    abstract fun getOne(releaseId: Int?, youtubeId: Int?): Single<FeedDb>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(
        items: List<FeedDb>,
        releaseDao: ReleaseDao,
        youtubeDao: YoutubeDao
    ): Completable {
        val actions = mutableListOf<Completable>()
        items.forEach { feedDb ->
            feedDb.release?.also { actions.add(releaseDao.insert(listOf(it))) }
            feedDb.youtube?.also { actions.add(youtubeDao.insert(listOf(it))) }
            actions.add(insert(feedDb.feed))
        }
        return Completable.concatArray(*actions.toTypedArray())
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(item: FlatFeedDb): Completable

    @Query("DELETE FROM feed")
    abstract fun deleteAll(): Completable
}