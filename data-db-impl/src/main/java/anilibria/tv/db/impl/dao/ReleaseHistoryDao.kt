package anilibria.tv.db.impl.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Single
import anilibria.tv.db.impl.entity.history.ReleaseHistoryDb

@Dao
interface ReleaseHistoryDao {

    @Query("SELECT * FROM `release_history`")
    fun getList(): Single<List<ReleaseHistoryDb>>

    @Query("SELECT * FROM release_history WHERE releaseId IN (:ids)")
    fun getList(ids: List<Int>): Single<List<ReleaseHistoryDb>>

    @Query("SELECT * FROM `release_history` WHERE releaseId = :releaseId LIMIT 1")
    fun getOne(releaseId: Int): Single<ReleaseHistoryDb>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items: List<ReleaseHistoryDb>): Completable

    @Query("DELETE FROM release_history WHERE releaseId IN (:ids)")
    fun delete(ids: List<Int>): Completable

    @Query("DELETE FROM release_history")
    fun deleteAll(): Completable
}