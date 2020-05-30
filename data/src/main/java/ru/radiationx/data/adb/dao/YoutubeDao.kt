package ru.radiationx.data.adb.dao

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single
import ru.radiationx.data.adb.entity.release.ReleaseDb
import ru.radiationx.data.adb.entity.youtube.YoutubeDb

@Dao
interface YoutubeDao {

    @Query("SELECT * FROM youtube")
    fun getListAll(): Single<List<YoutubeDb>>

    @Query("SELECT * FROM youtube WHERE youtubeId IN (:ids)")
    abstract fun getList(ids: List<Int>): Single<List<YoutubeDb>>

    @Query("SELECT * FROM youtube WHERE youtubeId = :youtubeId")
    fun getOne(youtubeId: Int): Single<YoutubeDb>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items: List<YoutubeDb>): Completable

    @Query("DELETE FROM youtube WHERE youtubeId IN (:ids)")
    abstract fun delete(ids: List<Int>): Completable

    @Query("DELETE FROM youtube")
    fun deleteAll(): Completable
}