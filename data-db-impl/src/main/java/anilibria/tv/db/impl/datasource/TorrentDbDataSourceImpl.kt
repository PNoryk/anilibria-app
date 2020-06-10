package anilibria.tv.db.impl.datasource

import anilibria.tv.db.TorrentDbDataSource
import anilibria.tv.db.impl.common.zipCollections
import io.reactivex.Completable
import io.reactivex.Single
import anilibria.tv.db.impl.converters.TorrentConverter
import anilibria.tv.db.impl.dao.TorrentDao
import anilibria.tv.db.impl.entity.torrent.TorrentDb
import anilibria.tv.domain.entity.common.keys.TorrentKey
import anilibria.tv.domain.entity.torrent.Torrent
import toothpick.InjectConstructor

@InjectConstructor
class TorrentDbDataSourceImpl(
    private val dao: TorrentDao,
    private val converter: TorrentConverter
) : TorrentDbDataSource {

    override fun getList(): Single<List<Torrent>> = dao
        .getList()
        .map(converter::toDomain)

    override fun getSome(keys: List<TorrentKey>): Single<List<Torrent>> = Single
        .defer {
            val fullKeys = converter.toDbKey(keys.filter { it.id != null })
            val releaseIds = keys.filter { it.id == null }.map { it.releaseId }

            val singles = mutableListOf<Single<List<TorrentDb>>>()
            if (fullKeys.isNotEmpty()) {
                singles.add(dao.getSome(fullKeys))
            }
            if (releaseIds.isNotEmpty()) {
                singles.add(dao.getSomeByReleases(releaseIds))
            }
            zipCollections(singles)
        }
        .map(converter::toDomain)

    override fun getOne(key: TorrentKey): Single<Torrent> = dao
        .getOne(converter.toDbKey(key))
        .map(converter::toDomain)

    override fun insert(items: List<Torrent>): Completable = Single.just(items)
        .map(converter::toDb)
        .flatMapCompletable(dao::insert)

    override fun remove(keys: List<TorrentKey>): Completable = Completable
        .fromAction {
            if (keys.any { it.id == null }) {
                throw IllegalArgumentException("All keys should contains not null id")
            }
        }
        .andThen(dao.remove(converter.toDbKey(keys)))

    override fun clear(): Completable = dao.clear()
}