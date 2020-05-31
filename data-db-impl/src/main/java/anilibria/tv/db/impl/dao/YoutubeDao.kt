package anilibria.tv.db.impl.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Single
import anilibria.tv.db.impl.entity.youtube.YoutubeDb

@Dao
interface YoutubeDao {

    @Query("SELECT * FROM youtube")
    fun getListAll(): Single<List<YoutubeDb>>

    @Query("SELECT * FROM youtube WHERE youtubeId IN (:ids)")
    abstract fun getList(ids: List<Int>): Single<List<YoutubeDb>>

    @Query("SELECT * FROM youtube WHERE youtubeId = :youtubeId LIMIT 1")
    fun getOne(youtubeId: Int): Single<YoutubeDb>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items: List<YoutubeDb>): Completable

    @Query("DELETE FROM youtube WHERE youtubeId IN (:ids)")
    abstract fun delete(ids: List<Int>): Completable

    @Query("DELETE FROM youtube")
    fun deleteAll(): Completable
}