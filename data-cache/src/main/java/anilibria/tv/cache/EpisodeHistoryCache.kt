package anilibria.tv.cache

import anilibria.tv.cache.common.ReadWriteCache
import anilibria.tv.domain.entity.episode.Episode
import anilibria.tv.domain.entity.relative.EpisodeHistoryRelative
import anilibria.tv.domain.entity.relative.ReleaseHistoryRelative
import io.reactivex.Observable
import io.reactivex.Single

interface EpisodeHistoryCache : ReadWriteCache<EpisodeHistoryRelative> {

    fun observeList(releaseIds: List<Int>): Observable<List<EpisodeHistoryRelative>>

    fun getList(releaseIds: List<Int>): Single<List<EpisodeHistoryRelative>>
}