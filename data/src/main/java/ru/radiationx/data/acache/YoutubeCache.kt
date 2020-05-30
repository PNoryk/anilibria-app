package ru.radiationx.data.acache

import io.reactivex.Observable
import io.reactivex.Single
import ru.radiationx.data.acache.common.ReadWriteCache
import ru.radiationx.data.acache.common.ReadableCache
import ru.radiationx.data.adomain.entity.release.Episode
import ru.radiationx.data.adomain.entity.release.Torrent
import ru.radiationx.data.adomain.entity.youtube.Youtube

interface YoutubeCache : ReadWriteCache<Youtube> {

    fun observeList(ids: List<Int>): Observable<List<Youtube>>

    fun getList(ids: List<Int>): Single<List<Youtube>>
}