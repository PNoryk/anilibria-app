package anilibria.tv.db.impl.datasource

import anilibria.tv.db.EpisodeDbDataSource
import anilibria.tv.db.EpisodeHistoryDbDataSource
import io.reactivex.Completable
import io.reactivex.Single
import anilibria.tv.db.impl.converters.EpisodeConverter
import anilibria.tv.db.impl.converters.EpisodeHistoryConverter
import anilibria.tv.db.impl.dao.EpisodeDao
import anilibria.tv.db.impl.dao.EpisodeHistoryDao
import anilibria.tv.db.impl.entity.history.EpisodeHistoryDb
import anilibria.tv.domain.entity.episode.Episode
import anilibria.tv.domain.entity.relative.EpisodeHistoryRelative
import anilibria.tv.domain.entity.relative.ReleaseHistoryRelative
import toothpick.InjectConstructor

@InjectConstructor
class EpisodeHistoryDbDataSourceImpl(
    private val dao: EpisodeHistoryDao,
    private val converter: EpisodeHistoryConverter
) : EpisodeHistoryDbDataSource {

    override fun getListAll(): Single<List<EpisodeHistoryRelative>> = dao
        .getListAll()
        .map(converter::toDomain)

    override fun getList(releaseIds: List<Int>): Single<List<EpisodeHistoryRelative>> = dao
        .getList(releaseIds)
        .map(converter::toDomain)

    override fun getListByPairIds(ids: List<Pair<Int, Int>>): Single<List<EpisodeHistoryRelative>> = dao
        .getListByKeys(converter.toDbKey(ids))
        .map(converter::toDomain)

    override fun getOne(releaseId: Int, episodeId: Int): Single<EpisodeHistoryRelative> = dao
        .getOne(releaseId, episodeId)
        .map(converter::toDomain)

    override fun insert(items: List<EpisodeHistoryRelative>): Completable = Single.just(items)
        .map(converter::toDb)
        .flatMapCompletable { dao.insert(it) }

    override fun removeList(ids: List<Pair<Int, Int>>): Completable = dao.delete(converter.toDbKey(ids))

    override fun deleteAll(): Completable = dao.deleteAll()
}