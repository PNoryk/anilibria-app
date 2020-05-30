package ru.radiationx.data.aarepo

import io.reactivex.Observable
import io.reactivex.Single
import ru.radiationx.data.acache.TorrentCache
import ru.radiationx.data.adomain.entity.release.Torrent
import toothpick.InjectConstructor

@InjectConstructor
class TorrentRepository(
    private val torrentCache: TorrentCache
) {

    fun observeList(): Observable<List<Torrent>> = torrentCache.observeList()

    fun observeListByRelease(releaseIds: List<Int>): Observable<List<Torrent>> = torrentCache.observeList(releaseIds)

    fun getList(): Single<List<Torrent>> = torrentCache.getList()

    fun getListByRelease(releaseIds: List<Int>): Single<List<Torrent>> = torrentCache.getList(releaseIds)

}