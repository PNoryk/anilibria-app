package anilibria.tv.db

import anilibria.tv.domain.entity.episode.Episode
import io.reactivex.Completable
import io.reactivex.Single

interface EpisodeDbDataSource {
    fun getListAll(): Single<List<Episode>>
    fun getList(releaseIds: List<Int>): Single<List<Episode>>
    fun getListByPairIds(ids: List<Pair<Int, Int>>): Single<List<Episode>>
    fun getOne(releaseId: Int, episodeId: Int): Single<Episode>
    fun insert(items: List<Episode>): Completable
    fun removeList(ids: List<Pair<Int, Int>>): Completable
    fun deleteAll(): Completable
}