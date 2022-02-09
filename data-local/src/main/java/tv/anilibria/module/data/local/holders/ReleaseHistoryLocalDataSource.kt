package tv.anilibria.module.data.local.holders

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import tv.anilibria.module.domain.entity.ReleaseVisit
import tv.anilibria.module.domain.entity.release.Release
import tv.anilibria.module.domain.entity.release.ReleaseId

interface ReleaseHistoryLocalDataSource {
    fun observe(): Observable<List<ReleaseVisit>>
    fun get(): Single<List<ReleaseVisit>>
    fun put(data: ReleaseVisit): Completable
    fun remove(releaseId: ReleaseId): Completable
}