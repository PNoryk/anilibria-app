package ru.radiationx.data.aarepo

import io.reactivex.Observable
import io.reactivex.Single
import ru.radiationx.data.acache.TorrentCache
import ru.radiationx.data.adomain.entity.release.Episode
import ru.radiationx.data.adomain.entity.release.Torrent
import toothpick.InjectConstructor

@InjectConstructor
class TorrentRepository(
    private val torrentCache: TorrentCache
) {

    fun observeList(): Observable<List<Torrent>> = torrentCache.observeList()

    fun observeListByRelease(releaseId: Int): Observable<List<Torrent>> = torrentCache
        .observeList()
        .map {
            it.filter { episode ->
                episode.releaseId == releaseId
            }
        }

    fun getList(): Single<List<Torrent>> = torrentCache.fetchList()

    fun getListByRelease(releaseId: Int): Single<List<Torrent>> = torrentCache
        .fetchList()
        .map {
            it.filter { episode ->
                episode.releaseId == releaseId
            }
        }

}