package tv.anilibria.module.data.network.datasource.holders

import io.reactivex.Observable
import io.reactivex.Single
import tv.anilibria.module.data.network.entity.app.release.ReleaseFull

interface EpisodesCheckerHolder {
    fun observeEpisodes(): Observable<MutableList<ReleaseFull.Episode>>
    fun getEpisodes(): Single<List<ReleaseFull.Episode>>
    fun putEpisode(episode: ReleaseFull.Episode)
    fun putAllEpisode(episodes: List<ReleaseFull.Episode>)
    fun getEpisodes(releaseId: Int): List<ReleaseFull.Episode>
    fun remove(releaseId: Int)
}