package ru.radiationx.data.acache

import io.reactivex.Observable
import io.reactivex.Single
import ru.radiationx.data.acache.common.ReadWriteCache
import ru.radiationx.data.acache.common.ReadableCache
import ru.radiationx.data.adomain.entity.release.Episode
import ru.radiationx.data.adomain.entity.release.Torrent

interface EpisodeCache : ReadWriteCache<Episode> {

    fun observeList(releaseIds: List<Int>): Observable<List<Episode>>

    fun getList(releaseIds: List<Int>): Single<List<Episode>>
}