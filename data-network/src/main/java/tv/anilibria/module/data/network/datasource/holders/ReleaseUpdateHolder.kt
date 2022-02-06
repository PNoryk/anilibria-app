package tv.anilibria.module.data.network.datasource.holders

import io.reactivex.Observable
import io.reactivex.Single
import tv.anilibria.module.data.network.entity.app.release.ReleaseItem
import tv.anilibria.module.data.network.entity.app.release.ReleaseUpdate

interface ReleaseUpdateHolder {
    fun observeEpisodes(): Observable<MutableList<ReleaseUpdate>>
    fun getReleases(): Single<List<ReleaseUpdate>>
    fun getRelease(id: Int): ReleaseUpdate?
    fun updRelease(release: ReleaseUpdate)
    fun updAllRelease(releases: List<ReleaseUpdate>)
    fun putRelease(release: ReleaseItem)
    fun putAllRelease(releases: List<ReleaseItem>)
}