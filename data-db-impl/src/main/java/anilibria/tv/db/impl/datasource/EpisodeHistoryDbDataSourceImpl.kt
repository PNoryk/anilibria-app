package anilibria.tv.db.impl.datasource

import anilibria.tv.db.EpisodeHistoryDbDataSource
import anilibria.tv.db.impl.common.zipCollections
import io.reactivex.Completable
import io.reactivex.Single
import anilibria.tv.db.impl.converters.EpisodeHistoryConverter
import anilibria.tv.db.impl.dao.EpisodeHistoryDao
import anilibria.tv.db.impl.entity.history.EpisodeHistoryDb
import anilibria.tv.domain.entity.common.keys.EpisodeKey
import anilibria.tv.domain.entity.relative.EpisodeHistoryRelative
import toothpick.InjectConstructor

@InjectConstructor
class EpisodeHistoryDbDataSourceImpl(
    private val dao: EpisodeHistoryDao,
    private val converter: EpisodeHistoryConverter
) : EpisodeHistoryDbDataSource {

    override fun getList(): Single<List<EpisodeHistoryRelative>> = dao
        .getList()
        .map(converter::toDomain)

    override fun getSome(keys: List<EpisodeKey>): Single<List<EpisodeHistoryRelative>> = Single
        .defer {
            val fullKeys = converter.toDbKey(keys.filter { it.id != null })
            val releaseIds = keys.filter { it.id == null }.map { it.releaseId }

            val singles = mutableListOf<Single<List<EpisodeHistoryDb>>>()
            if (fullKeys.isNotEmpty()) {
                singles.add(dao.getSome(fullKeys))
            }
            if (releaseIds.isNotEmpty()) {
                singles.add(dao.getSomeByReleases(releaseIds))
            }
            zipCollections(singles)
        }
        .map(converter::toDomain)

    override fun getOne(key: EpisodeKey): Single<EpisodeHistoryRelative> = dao
        .getOne(converter.toDbKey(key))
        .map(converter::toDomain)

    override fun insert(items: List<EpisodeHistoryRelative>): Completable = Single.just(items)
        .map(converter::toDb)
        .flatMapCompletable { dao.insert(it) }

    override fun remove(keys: List<EpisodeKey>): Completable = Completable
        .fromAction {
            if (keys.any { it.id == null }) {
                throw IllegalArgumentException("All keys should contains not null id")
            }
        }
        .andThen(dao.remove(converter.toDbKey(keys)))

    override fun clear(): Completable = dao.clear()
}