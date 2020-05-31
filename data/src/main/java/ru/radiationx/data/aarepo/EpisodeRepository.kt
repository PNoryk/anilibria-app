package ru.radiationx.data.aarepo

import io.reactivex.Observable
import io.reactivex.Single
import ru.radiationx.data.acache.EpisodeCache
import anilibria.tv.domain.entity.release.Episode
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