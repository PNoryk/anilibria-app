package ru.radiationx.data.adb.dao

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single
import ru.radiationx.data.adb.episode.EpisodeDb
import ru.radiationx.data.adb.youtube.YouTubeDb

@Dao
interface YoutubeDao {

    @Query("SELECT * FROM youtube")
    fun getListAll(): Single<List<YouTubeDb>>

    @Query("SELECT * FROM youtube WHERE youtubeId = :youtubeId")
    fun getList(youtubeId: Int): Single<List<YouTubeDb>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items: List<YouTubeDb>): Completable

    @Delete
    fun delete(items: List<YouTubeDb>): Completable
}