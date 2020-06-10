package anilibria.tv.db

import anilibria.tv.domain.entity.common.keys.ReleaseKey
import anilibria.tv.domain.entity.relative.ReleaseHistoryRelative
import io.reactivex.Completable
import io.reactivex.Single

interface ReleaseHistoryDbDataSource {
    fun getList(): Single<List<ReleaseHistoryRelative>>
    fun getSome(keys: List<ReleaseKey>): Single<List<ReleaseHistoryRelative>>
    fun getOne(key:ReleaseKey): Single<ReleaseHistoryRelative>
    fun insert(items: List<ReleaseHistoryRelative>): Completable
    fun remove(keys: List<ReleaseKey>): Completable
    fun clear(): Completable
}