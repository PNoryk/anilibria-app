package ru.radiationx.data.aarepo

import io.reactivex.Observable
import io.reactivex.Single
import anilibria.tv.cache.EpisodeCache
import anilibria.tv.domain.entity.episode.Episode
import toothpick.InjectConstructor

@InjectConstructor
class EpisodeRepository(
    private val episodeCache: EpisodeCache
) {

    fun observeList(): Observable<List<Episode>> = episodeCache.observeList()

    fun observeList(releaseIds: List<Int>): Observable<List<Episode>> = episodeCache.observeList(releaseIds)

    fun getList(): Single<List<Episode>> = episodeCache.getList()

    fun getList(releaseIds: List<Int>): Single<List<Episode>> = episodeCache.getList(releaseIds)

}