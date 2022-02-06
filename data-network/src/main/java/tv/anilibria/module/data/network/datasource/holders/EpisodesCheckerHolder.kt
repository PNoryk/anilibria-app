package tv.anilibria.module.data.network.datasource.holders

import io.reactivex.Observable
import io.reactivex.Single
import tv.anilibria.module.data.network.entity.app.release.EpisodeResponse

interface EpisodesCheckerHolder {
    fun observeEpisodes(): Observable<MutableList<EpisodeResponse>>
    fun getEpisodes(): Single<List<EpisodeResponse>>
    fun putEpisode(episode: EpisodeResponse)
    fun putAllEpisode(episodes: List<EpisodeResponse>)
    fun getEpisodes(releaseId: Int): List<EpisodeResponse>
    fun remove(releaseId: Int)
}