package ru.radiationx.data.adb.dao

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single
import ru.radiationx.data.adb.favorite.FavoriteDb
import ru.radiationx.data.adb.favorite.FlatFavoriteDb
import ru.radiationx.data.adb.feed.FeedDb
import ru.radiationx.data.adb.history.FlatHistoryDb
import ru.radiationx.data.adb.history.HistoryDb
import ru.radiationx.data.adb.torrent.TorrentDb

@Dao
abstract class HistoryDao {

    @Transaction
    @Query("SELECT * FROM `history`")
    abstract fun getList(): Single<List<HistoryDb>>

    @Transaction
    @Query("SELECT * FROM `history` WHERE releaseId = :releaseId")
    abstract fun getOne(releaseId: Int): Single<HistoryDb>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(
        items: List<HistoryDb>,
        releaseDao: ReleaseDao
    ): Completable {
        val actions = mutableListOf<Completable>()
        items.forEach { historyDb ->
            historyDb.release.also { actions.add(releaseDao.insert(listOf(it))) }
            actions.add(insert(historyDb.history))
        }
        return Completable.concatArray(*actions.toTypedArray())
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(item: FlatHistoryDb): Completable
}