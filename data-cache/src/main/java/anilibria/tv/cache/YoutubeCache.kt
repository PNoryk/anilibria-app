package anilibria.tv.cache

import io.reactivex.Observable
import io.reactivex.Single
import anilibria.tv.cache.common.ReadWriteCache
import anilibria.tv.domain.entity.youtube.Youtube

interface YoutubeCache : ReadWriteCache<Youtube> {

    fun observeList(ids: List<Int>): Observable<List<Youtube>>

    fun getList(ids: List<Int>): Single<List<Youtube>>
}