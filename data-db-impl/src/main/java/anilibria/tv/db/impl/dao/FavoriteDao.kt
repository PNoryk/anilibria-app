package anilibria.tv.db.impl.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Single
import anilibria.tv.db.impl.entity.favorite.FavoriteDb

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM `favorite`")
    fun getSome(): Single<List<FavoriteDb>>

    @Query("SELECT * FROM favorite WHERE releaseId IN (:ids)")
    fun getSome(ids: List<Int>): Single<List<FavoriteDb>>

    @Query("SELECT * FROM `favorite` WHERE releaseId = :releaseId LIMIT 1")
    fun getOne(releaseId: Int): Single<FavoriteDb>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items: List<FavoriteDb>): Completable

    @Query("DELETE FROM favorite WHERE releaseId IN (:ids)")
    fun remove(ids: List<Int>): Completable

    @Query("DELETE FROM favorite")
    fun clear(): Completable
}