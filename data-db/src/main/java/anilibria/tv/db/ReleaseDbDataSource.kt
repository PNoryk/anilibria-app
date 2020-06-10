package anilibria.tv.db

import anilibria.tv.domain.entity.common.keys.ReleaseKey
import anilibria.tv.domain.entity.release.Release
import io.reactivex.Completable
import io.reactivex.Single

interface ReleaseDbDataSource {
    fun getList(): Single<List<Release>>
    fun getSome(keys: List<ReleaseKey>): Single<List<Release>>
    fun getOne(key: ReleaseKey): Single<Release>
    fun insert(items: List<Release>): Completable
    fun remove(keys: List<ReleaseKey>): Completable
    fun clear(): Completable
}