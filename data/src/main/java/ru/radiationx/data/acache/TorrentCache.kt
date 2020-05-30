package ru.radiationx.data.acache

import io.reactivex.Single
import ru.radiationx.data.acache.common.ReadWriteCache
import ru.radiationx.data.acache.common.ReadableCache
import ru.radiationx.data.adomain.entity.release.Episode
import ru.radiationx.data.adomain.entity.release.Torrent

interface TorrentCache : ReadWriteCache<Torrent>{

    fun getList(releaseId: Int): Single<List<Torrent>>
}