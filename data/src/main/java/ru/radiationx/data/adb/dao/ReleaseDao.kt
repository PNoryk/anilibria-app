package ru.radiationx.data.adb.dao

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single
import ru.radiationx.data.adb.release.BlockInfoDb
import ru.radiationx.data.adb.release.FavoriteInfoDb
import ru.radiationx.data.adb.release.FlatReleaseDb
import ru.radiationx.data.adb.release.ReleaseDb

@Dao
abstract class ReleaseDao {

    @Transaction
    @Query("SELECT * FROM `release`")
    abstract fun getListAll(): Single<List<ReleaseDb>>

    @Transaction
    @Query("SELECT * FROM `release` WHERE releaseId = :releaseId")
    abstract fun getList(releaseId: Int): Single<List<ReleaseDb>>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items: List<ReleaseDb>): Completable {
        val actions = mutableListOf<Completable>()
        items.forEach { releaseDb ->
            actions.add(insert(releaseDb.release))
            releaseDb.favorite?.also { actions.add(insert(it)) }
            releaseDb.blockedInfo?.also { actions.add(insert(it)) }
        }
        return Completable.concatArray(*actions.toTypedArray())
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(release: FlatReleaseDb): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(favoriteInfo: FavoriteInfoDb): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(blockInfoDb: BlockInfoDb): Completable

    @Delete
    abstract fun delete(items: List<FlatReleaseDb>): Completable

}