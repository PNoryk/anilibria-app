package ru.radiationx.data.aarepo

import io.reactivex.Observable
import io.reactivex.Single
import ru.radiationx.data.acache.EpisodeCache
import ru.radiationx.data.adomain.entity.release.Episode
import toothpick.InjectConstructor

@InjectConstructor
class EpisodeRepository(
    private val episodeCache: EpisodeCache
) {

    fun observeList(): Observable<List<Episode>> = episodeCache.observeList()

    fun observeListByRelease(releaseId: Int): Observable<List<Episode>> = episodeCache
        .observeList()
        .map {
            it.filter { episode ->
                episode.releaseId == releaseId
            }
        }

    fun getList(): Single<List<Episode>> = episodeCache.getList()

    fun getListByRelease(releaseId: Int): Single<List<Episode>> = episodeCache
        .getList()
        .map {
            it.filter { episode ->
                episode.releaseId == releaseId
            }
        }

}