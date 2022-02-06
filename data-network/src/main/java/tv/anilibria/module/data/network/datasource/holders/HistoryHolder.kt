package tv.anilibria.module.data.network.datasource.holders

import io.reactivex.Observable
import io.reactivex.Single
import tv.anilibria.module.data.network.entity.app.release.ReleaseItem

interface HistoryHolder {
    fun getEpisodes(): Single<List<ReleaseItem>>
    fun observeEpisodes(): Observable<MutableList<ReleaseItem>>
    fun putRelease(release: ReleaseItem)
    fun putAllRelease(releases: List<ReleaseItem>)
    fun removerRelease(id: Int)
}