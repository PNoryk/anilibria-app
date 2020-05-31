package anilibria.tv.db.impl.datasource

import anilibria.tv.db.TorrentDbDataSource
import io.reactivex.Completable
import io.reactivex.Single
import anilibria.tv.db.impl.converters.TorrentConverter
import anilibria.tv.db.impl.dao.TorrentDao
import anilibria.tv.domain.entity.release.Torrent
import toothpick.InjectConstructor

@InjectConstructor
class TorrentDbDataSourceImpl(
    private val dao: TorrentDao,
    private val converter: TorrentConverter
) : TorrentDbDataSource {

    override fun getListAll(): Single<List<Torrent>> = dao
        .getListAll()
        .map(converter::toDomain)

    override fun getList(releaseIds: List<Int>): Single<List<Torrent>> = dao
        .getList(releaseIds)
        .map(converter::toDomain)

    override fun getListByPairIds(ids: List<Pair<Int, Int>>): Single<List<Torrent>> = dao
        .getListByKeys(converter.toDbKey(ids))
        .map(converter::toDomain)

    override fun getOne(releaseId: Int, torrentId: Int): Single<Torrent> = dao
        .getOne(releaseId, torrentId)
        .map(converter::toDomain)

    override fun insert(items: List<Torrent>): Completable = Single.just(items)
        .map(converter::toDb)
        .flatMapCompletable(dao::insert)

    override fun removeList(ids: List<Pair<Int, Int>>): Completable = dao.delete(converter.toDbKey(ids))

    override fun deleteAll(): Completable = dao.deleteAll()
}