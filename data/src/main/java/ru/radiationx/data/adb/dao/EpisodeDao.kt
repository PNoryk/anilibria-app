package ru.radiationx.data.adb.dao

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single
import ru.radiationx.data.adb.episode.EpisodeDb

@Dao
interface EpisodeDao {

    @Query("SELECT * FROM release_episode")
    fun getListAll(): Single<List<EpisodeDb>>

    @Query("SELECT * FROM release_episode WHERE releaseId = :releaseId")
    fun getList(releaseId: Int): Single<List<EpisodeDb>>

    @Query("SELECT * FROM release_episode WHERE releaseId = :releaseId and id = :episodeId")
    fun getOne(releaseId: Int, episodeId: Int): Single<EpisodeDb>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSome(items: List<EpisodeDb>): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOne(item: EpisodeDb): Completable

    @Delete
    fun deleteSome(items: List<EpisodeDb>): Completable

    @Delete
    fun deleteOne(item: EpisodeDb): Completable
}