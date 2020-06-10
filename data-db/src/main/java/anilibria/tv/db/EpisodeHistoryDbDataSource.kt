package anilibria.tv.db

import anilibria.tv.domain.entity.common.keys.EpisodeKey
import anilibria.tv.domain.entity.episode.Episode
import anilibria.tv.domain.entity.relative.EpisodeHistoryRelative
import anilibria.tv.domain.entity.relative.ReleaseHistoryRelative
import io.reactivex.Completable
import io.reactivex.Single

interface EpisodeHistoryDbDataSource {
    fun getList(): Single<List<EpisodeHistoryRelative>>
    fun getSome(keys: List<EpisodeKey>): Single<List<EpisodeHistoryRelative>>
    fun getOne(key: EpisodeKey): Single<EpisodeHistoryRelative>
    fun insert(items: List<EpisodeHistoryRelative>): Completable
    fun remove(keys: List<EpisodeKey>): Completable
    fun clear(): Completable
}