package ru.radiationx.data.acache.combiner

import io.reactivex.Observable
import io.reactivex.Single
import ru.radiationx.data.acache.common.ReadWriteCache
import ru.radiationx.data.acache.common.ReadableCache
import ru.radiationx.data.adomain.entity.release.Episode
import ru.radiationx.data.adomain.entity.release.Release

interface ReleaseCacheCombiner : ReadWriteCache<Release> {

    fun observeOne(releaseId: Int? = null, releaseCode: String? = null): Observable<Release>

    fun observeList(ids: List<Int>? = null, codes: List<String>? = null): Observable<List<Release>>

    fun fetchOne(releaseId: Int? = null, releaseCode: String? = null): Single<Release>

    fun fetchList(ids: List<Int>? = null, codes: List<String>? = null): Single<List<Release>>
}