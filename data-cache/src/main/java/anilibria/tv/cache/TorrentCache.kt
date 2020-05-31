package anilibria.tv.cache

import io.reactivex.Observable
import io.reactivex.Single
import anilibria.tv.cache.common.ReadWriteCache
import anilibria.tv.domain.entity.torrent.Torrent

interface TorrentCache : ReadWriteCache<Torrent>{

    fun observeList(releaseIds: List<Int>): Observable<List<Torrent>>

    fun getList(releaseIds: List<Int>): Single<List<Torrent>>
}