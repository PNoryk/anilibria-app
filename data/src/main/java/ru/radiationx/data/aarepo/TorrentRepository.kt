package ru.radiationx.data.aarepo

import io.reactivex.Observable
import io.reactivex.Single
import anilibria.tv.cache.TorrentCache
import anilibria.tv.domain.entity.common.keys.EpisodeKey
import anilibria.tv.domain.entity.common.keys.TorrentKey
import anilibria.tv.domain.entity.torrent.Torrent
import toothpick.InjectConstructor

@InjectConstructor
class TorrentRepository(
    private val torrentCache: TorrentCache
) {

    fun observeList(): Observable<List<Torrent>> = torrentCache.observeList()

    fun observeListByRelease(releaseIds: List<Int>): Observable<List<Torrent>> = torrentCache.observeSome(releaseIds.toKey())

    fun getList(): Single<List<Torrent>> = torrentCache.getList()

    fun getListByRelease(releaseIds: List<Int>): Single<List<Torrent>> = torrentCache.getSome(releaseIds.toKey())


    private fun List<Int>.toKey() = map { TorrentKey(it, null) }
}