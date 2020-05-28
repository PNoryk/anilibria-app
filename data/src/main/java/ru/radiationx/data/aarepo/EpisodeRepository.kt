package ru.radiationx.data.aarepo

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import ru.radiationx.data.acache.EpisodeCache
import ru.radiationx.data.adomain.entity.history.HistoryItem
import ru.radiationx.data.adomain.entity.release.Episode
import ru.radiationx.data.adomain.entity.release.Release
import toothpick.InjectConstructor
import java.util.*

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

    fun getList(): Single<List<Episode>> = episodeCache.fetchList()

    fun getListByRelease(releaseId: Int): Single<List<Episode>> = episodeCache
        .fetchList()
        .map {
            it.filter { episode ->
                episode.releaseId == releaseId
            }
        }

}