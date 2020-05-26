package ru.radiationx.data.adb.dao

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single
import ru.radiationx.data.adb.episode.EpisodeDb
import ru.radiationx.data.adb.release.FlatReleaseDb
import ru.radiationx.data.adb.release.ReleaseDb
import ru.radiationx.data.adb.youtube.YouTubeDb

@Dao
interface ReleaseDao {

    @Query("SELECT * FROM `release`")
    fun getListAll(): Single<List<ReleaseDb>>

    @Query("SELECT * FROM `release` WHERE releaseId = :releaseId")
    fun getList(releaseId: Int): Single<List<ReleaseDb>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSome(items: List<FlatReleaseDb>): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOne(item: FlatReleaseDb): Completable

    @Delete
    fun deleteSome(items: List<FlatReleaseDb>): Completable

    @Delete
    fun deleteOne(item: FlatReleaseDb): Completable
}