package tv.anilibria.module.data.network.datasource.holders

import io.reactivex.Observable
import io.reactivex.Single
import tv.anilibria.module.data.network.entity.app.release.ReleaseResponse

interface HistoryHolder {
    fun getEpisodes(): Single<List<ReleaseResponse>>
    fun observeEpisodes(): Observable<MutableList<ReleaseResponse>>
    fun putRelease(release: ReleaseResponse)
    fun putAllRelease(releases: List<ReleaseResponse>)
    fun removerRelease(id: Int)
}