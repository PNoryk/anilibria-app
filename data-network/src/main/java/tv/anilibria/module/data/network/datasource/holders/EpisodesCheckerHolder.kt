package tv.anilibria.module.data.network.datasource.holders

import io.reactivex.Observable
import io.reactivex.Single
import tv.anilibria.module.data.network.entity.app.release.ReleaseResponse

interface EpisodesCheckerHolder {
    fun observeEpisodes(): Observable<MutableList<ReleaseResponse.Episode>>
    fun getEpisodes(): Single<List<ReleaseResponse.Episode>>
    fun putEpisode(episode: ReleaseResponse.Episode)
    fun putAllEpisode(episodes: List<ReleaseResponse.Episode>)
    fun getEpisodes(releaseId: Int): List<ReleaseResponse.Episode>
    fun remove(releaseId: Int)
}