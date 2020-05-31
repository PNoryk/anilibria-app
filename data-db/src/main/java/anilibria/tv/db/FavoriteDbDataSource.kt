package anilibria.tv.db

import anilibria.tv.domain.entity.relative.FavoriteRelative
import io.reactivex.Completable
import io.reactivex.Single

interface FavoriteDbDataSource {
    fun getListAll(): Single<List<FavoriteRelative>>
    fun getList(ids: List<Int>): Single<List<FavoriteRelative>>
    fun getOne(releaseId: Int): Single<FavoriteRelative>
    fun insert(items: List<FavoriteRelative>): Completable
    fun removeList(ids: List<Int>): Completable
    fun deleteAll(): Completable
}