package anilibria.tv.cache

import anilibria.tv.cache.common.ReadWriteCache
import anilibria.tv.domain.entity.common.keys.EpisodeKey
import anilibria.tv.domain.entity.relative.EpisodeHistoryRelative
import io.reactivex.Observable
import io.reactivex.Single

interface EpisodeHistoryCache : ReadWriteCache<EpisodeKey, EpisodeHistoryRelative> {

    fun observeSome(keys: List<EpisodeKey>): Observable<List<EpisodeHistoryRelative>>

    fun getSome(keys: List<EpisodeKey>): Single<List<EpisodeHistoryRelative>>
}