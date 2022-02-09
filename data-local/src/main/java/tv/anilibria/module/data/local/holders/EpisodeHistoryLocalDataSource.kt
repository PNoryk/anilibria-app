package tv.anilibria.module.data.local.holders

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import tv.anilibria.module.domain.entity.EpisodeVisit
import tv.anilibria.module.domain.entity.release.EpisodeId
import tv.anilibria.module.domain.entity.release.ReleaseId

interface EpisodeHistoryLocalDataSource {
    fun observe(): Observable<List<EpisodeVisit>>
    fun get(): Single<List<EpisodeVisit>>
    fun put(data: EpisodeVisit): Completable
    fun remove(episodeId: EpisodeId): Completable
    fun removeByRelease(releaseId: ReleaseId): Completable
}