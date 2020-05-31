package anilibria.tv.db.impl.dao

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single
import anilibria.tv.db.impl.entity.release.BlockInfoDb
import anilibria.tv.db.impl.entity.release.FavoriteInfoDb
import anilibria.tv.db.impl.entity.release.FlatReleaseDb
import anilibria.tv.db.impl.entity.release.ReleaseDb

@Dao
abstract class ReleaseDao {

    @Transaction
    @Query("SELECT * FROM `release`")
    abstract fun getListAll(): Single<List<ReleaseDb>>

    @Query("SELECT * FROM `release` WHERE releaseId IN (:ids) OR code IN (:codes)")
    abstract fun getList(ids: List<Int>, codes: List<String>): Single<List<ReleaseDb>>

    @Transaction
    @Query("SELECT * FROM `release` WHERE releaseId = :releaseId OR code = :code LIMIT 1")
    abstract fun getOne(releaseId: Int?, code: String?): Single<ReleaseDb>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items: List<ReleaseDb>): Completable {
        val actions = mutableListOf<Completable>()
        items.forEach { releaseDb ->
            actions.add(insert(releaseDb.release))
            releaseDb.favorite?.also { actions.add(insertFavorite(it)) }
            releaseDb.blockedInfo?.also { actions.add(insertBlockInfo(it)) }
        }
        return Completable.concatArray(*actions.toTypedArray())
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(release: FlatReleaseDb): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertFavorite(favoriteInfo: FavoriteInfoDb): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertBlockInfo(blockInfoDb: BlockInfoDb): Completable

    @Query("DELETE FROM `release` WHERE releaseId IN (:ids)")
    abstract fun delete(ids: List<Int>): Completable

    @Query("DELETE FROM `release`")
    abstract fun deleteAll(): Completable
}