package anilibria.tv.db

import anilibria.tv.domain.entity.youtube.Youtube
import io.reactivex.Completable
import io.reactivex.Single

interface YoutubeDbDataSource {
    fun getListAll(): Single<List<Youtube>>
    fun getList(ids: List<Int>): Single<List<Youtube>>
    fun getOne(youtubeId: Int): Single<Youtube>
    fun insert(items: List<Youtube>): Completable
    fun removeList(ids: List<Int>): Completable
    fun deleteAll(): Completable
}