package ru.radiationx.data.acache

import io.reactivex.Observable
import io.reactivex.Single
import ru.radiationx.data.acache.common.ReadWriteCache
import ru.radiationx.data.acache.common.ReadableCache
import ru.radiationx.data.adomain.entity.release.Episode
import ru.radiationx.data.adomain.entity.release.Torrent
import ru.radiationx.data.adomain.entity.youtube.Youtube

interface TorrentCache : ReadWriteCache<Torrent>{

    fun observeList(releaseIds: List<Int>): Observable<List<Torrent>>

    fun getList(releaseIds: List<Int>): Single<List<Torrent>>
}