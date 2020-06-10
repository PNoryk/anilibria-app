package anilibria.tv.db.impl.datasource

import anilibria.tv.db.EpisodeDbDataSource
import anilibria.tv.db.impl.common.zipCollections
import io.reactivex.Completable
import io.reactivex.Single
import anilibria.tv.db.impl.converters.EpisodeConverter
import anilibria.tv.db.impl.dao.EpisodeDao
import anilibria.tv.db.impl.entity.episode.EpisodeDb
import anilibria.tv.domain.entity.common.keys.EpisodeKey
import anilibria.tv.domain.entity.episode.Episode
import toothpick.InjectConstructor

@InjectConstructor
class EpisodeDbDataSourceImpl(
    private val dao: EpisodeDao,
    private val converter: EpisodeConverter
) : EpisodeDbDataSource {

    override fun getList(): Single<List<Episode>> = dao
        .getListAll()
        .map(converter::toDomain)

    override fun getSome(keys: List<EpisodeKey>): Single<List<Episode>> = Single
        .defer {
            val fullKeys = converter.toDbKey(keys.filter { it.id != null })
            val releaseIds = keys.filter { it.id == null }.map { it.releaseId }

            val singles = mutableListOf<Single<List<EpisodeDb>>>()
            if (fullKeys.isNotEmpty()) {
                singles.add(dao.getSome(fullKeys))
            }
            if (releaseIds.isNotEmpty()) {
                singles.add(dao.getSomeByReleases(releaseIds))
            }
            zipCollections(singles)
        }
        .map(converter::toDomain)

    override fun getOne(key: EpisodeKey): Single<Episode> = dao
        .getOne(converter.toDbKey(key))
        .map(converter::toDomain)

    override fun insert(items: List<Episode>): Completable = Single.just(items)
        .map(converter::toDb)
        .flatMapCompletable(dao::insert)

    override fun remove(keys: List<EpisodeKey>): Completable = Completable
        .fromAction {
            if (keys.any { it.id == null }) {
                throw IllegalArgumentException("All keys should contains not null id")
            }
        }
        .andThen(dao.remove(converter.toDbKey(keys)))

    override fun clear(): Completable = dao.clear()
}