package ru.radiationx.data.adb.dao

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single
import ru.radiationx.data.adb.entity.favorite.FavoriteDb

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM `favorite`")
    fun getList(): Single<List<FavoriteDb>>

    @Query("SELECT * FROM `favorite` WHERE releaseId = :releaseId")
    fun getOne(releaseId: Int): Single<FavoriteDb>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items: List<FavoriteDb>): Completable

    @Query("DELETE FROM favorite")
    fun deleteAll(): Completable
}