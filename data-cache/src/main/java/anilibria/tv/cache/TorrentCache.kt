package anilibria.tv.cache

import io.reactivex.Observable
import io.reactivex.Single
import anilibria.tv.cache.common.ReadWriteCache
import anilibria.tv.domain.entity.common.keys.TorrentKey
import anilibria.tv.domain.entity.torrent.Torrent

interface TorrentCache : ReadWriteCache<TorrentKey, Torrent> {

    fun observeSome(keys: List<TorrentKey>): Observable<List<Torrent>>

    fun getSome(keys: List<TorrentKey>): Single<List<Torrent>>
}