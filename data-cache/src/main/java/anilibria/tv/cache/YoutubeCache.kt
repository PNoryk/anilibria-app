package anilibria.tv.cache

import io.reactivex.Observable
import io.reactivex.Single
import anilibria.tv.cache.common.ReadWriteCache
import anilibria.tv.domain.entity.common.keys.YoutubeKey
import anilibria.tv.domain.entity.youtube.Youtube

interface YoutubeCache : ReadWriteCache<YoutubeKey, Youtube> {

    fun observeSome(keys: List<YoutubeKey>): Observable<List<Youtube>>

    fun getSome(keys: List<YoutubeKey>): Single<List<Youtube>>
}