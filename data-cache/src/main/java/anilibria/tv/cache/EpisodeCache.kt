package anilibria.tv.cache

import io.reactivex.Observable
import io.reactivex.Single
import anilibria.tv.cache.common.ReadWriteCache
import anilibria.tv.domain.entity.episode.Episode

interface EpisodeCache : ReadWriteCache<Episode> {

    fun observeList(releaseIds: List<Int>): Observable<List<Episode>>

    fun getList(releaseIds: List<Int>): Single<List<Episode>>
}