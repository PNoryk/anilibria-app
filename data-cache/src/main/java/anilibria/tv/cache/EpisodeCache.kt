package anilibria.tv.cache

import io.reactivex.Observable
import io.reactivex.Single
import anilibria.tv.cache.common.ReadWriteCache
import anilibria.tv.domain.entity.common.keys.EpisodeKey
import anilibria.tv.domain.entity.episode.Episode

interface EpisodeCache : ReadWriteCache<EpisodeKey, Episode> {

    fun observeSome(keys: List<EpisodeKey>): Observable<List<Episode>>

    fun getSome(keys: List<EpisodeKey>): Single<List<Episode>>
}