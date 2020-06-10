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
    fun getList(): Single<List<YoutubeDb>>

    @Query("SELECT * FROM youtube WHERE id IN (:ids)")
    fun getSome(ids: List<Int>): Single<List<YoutubeDb>>

    @Query("SELECT * FROM youtube WHERE id = :id LIMIT 1")
    fun getOne(id: Int): Single<YoutubeDb>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items: List<YoutubeDb>): Completable

    @Query("DELETE FROM youtube WHERE id IN (:ids)")
    fun remove(ids: List<Int>): Completable

    @Query("DELETE FROM youtube")
    fun clear(): Completable
}