package anilibria.tv.db

import anilibria.tv.domain.entity.common.keys.ReleaseKey
import anilibria.tv.domain.entity.relative.FavoriteRelative
import io.reactivex.Completable
import io.reactivex.Single

interface FavoriteDbDataSource {
    fun getList(): Single<List<FavoriteRelative>>
    fun getSome(keys: List<ReleaseKey>): Single<List<FavoriteRelative>>
    fun getOne(key: ReleaseKey): Single<FavoriteRelative>
    fun insert(items: List<FavoriteRelative>): Completable
    fun remove(keys: List<ReleaseKey>): Completable
    fun clear(): Completable
}