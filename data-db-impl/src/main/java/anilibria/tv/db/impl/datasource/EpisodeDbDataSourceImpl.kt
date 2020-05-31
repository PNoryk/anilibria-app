package anilibria.tv.db.impl.datasource

import anilibria.tv.db.EpisodeDbDataSource
import io.reactivex.Completable
import io.reactivex.Single
import anilibria.tv.db.impl.converters.EpisodeConverter
import anilibria.tv.db.impl.dao.EpisodeDao
import anilibria.tv.domain.entity.episode.Episode
import toothpick.InjectConstructor

@InjectConstructor
class EpisodeDbDataSourceImpl(
    private val dao: EpisodeDao,
    private val converter: EpisodeConverter
) : EpisodeDbDataSource {

    override fun getListAll(): Single<List<Episode>> = dao
        .getListAll()
        .map(converter::toDomain)

    override fun getList(releaseIds: List<Int>): Single<List<Episode>> = dao
        .getList(releaseIds)
        .map(converter::toDomain)

    override fun getListByPairIds(ids: List<Pair<Int, Int>>): Single<List<Episode>> = dao
        .getListByKeys(converter.toDbKey(ids))
        .map(converter::toDomain)

    override fun getOne(releaseId: Int, episodeId: Int): Single<Episode> = dao
        .getOne(releaseId, episodeId)
        .map(converter::toDomain)

    override fun insert(items: List<Episode>): Completable = Single.just(items)
        .map(converter::toDb)
        .flatMapCompletable(dao::insert)

    override fun removeList(ids: List<Pair<Int, Int>>): Completable = dao.delete(converter.toDbKey(ids))

    override fun deleteAll(): Completable = dao.deleteAll()
}