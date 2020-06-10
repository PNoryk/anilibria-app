package anilibria.tv.db

import anilibria.tv.domain.entity.common.keys.EpisodeKey
import anilibria.tv.domain.entity.episode.Episode
import io.reactivex.Completable
import io.reactivex.Single

interface EpisodeDbDataSource {
    fun getList(): Single<List<Episode>>
    fun getSome(keys: List<EpisodeKey>): Single<List<Episode>>
    fun getOne(key: EpisodeKey): Single<Episode>
    fun insert(items: List<Episode>): Completable
    fun remove(keys: List<EpisodeKey>): Completable
    fun clear(): Completable
}