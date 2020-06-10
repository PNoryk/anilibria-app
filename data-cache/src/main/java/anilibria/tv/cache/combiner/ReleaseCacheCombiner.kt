package anilibria.tv.cache.combiner

import io.reactivex.Observable
import io.reactivex.Single
import anilibria.tv.cache.common.ReadWriteCache
import anilibria.tv.domain.entity.common.keys.ReleaseKey
import anilibria.tv.domain.entity.release.Release

interface ReleaseCacheCombiner : ReadWriteCache<ReleaseKey, Release> {

    fun observeOne(key: ReleaseKey): Observable<Release>

    fun observeSome(keys: List<ReleaseKey>): Observable<List<Release>>

    fun getOne(key: ReleaseKey): Single<Release>

    fun getSome(keys: List<ReleaseKey>): Single<List<Release>>
}