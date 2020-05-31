package anilibria.tv.db

import anilibria.tv.domain.entity.episode.Episode
import anilibria.tv.domain.entity.relative.EpisodeHistoryRelative
import anilibria.tv.domain.entity.relative.ReleaseHistoryRelative
import io.reactivex.Completable
import io.reactivex.Single

interface EpisodeHistoryDbDataSource {
    fun getListAll(): Single<List<EpisodeHistoryRelative>>
    fun getList(releaseIds: List<Int>): Single<List<EpisodeHistoryRelative>>
    fun getListByPairIds(ids: List<Pair<Int, Int>>): Single<List<EpisodeHistoryRelative>>
    fun getOne(releaseId: Int, episodeId: Int): Single<EpisodeHistoryRelative>
    fun insert(items: List<EpisodeHistoryRelative>): Completable
    fun removeList(ids: List<Pair<Int, Int>>): Completable
    fun deleteAll(): Completable
}