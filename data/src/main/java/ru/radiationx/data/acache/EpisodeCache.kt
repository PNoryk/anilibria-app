package ru.radiationx.data.acache

import io.reactivex.Single
import ru.radiationx.data.acache.common.ReadWriteCache
import ru.radiationx.data.acache.common.ReadableCache
import ru.radiationx.data.adomain.entity.release.Episode

interface EpisodeCache : ReadWriteCache<Episode> {

    fun getList(releaseId: Int): Single<List<Episode>>
}