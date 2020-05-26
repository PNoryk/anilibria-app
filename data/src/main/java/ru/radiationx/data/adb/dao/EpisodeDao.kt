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
    fun insert(items: List<EpisodeDb>): Completable

    @Delete
    fun delete(items: List<EpisodeDb>): Completable
}