package ru.radiationx.data.adb.datasource

import io.reactivex.Completable
import io.reactivex.Single
import ru.radiationx.data.adb.converters.TorrentConverter
import ru.radiationx.data.adb.dao.TorrentDao
import anilibria.tv.domain.entity.release.Torrent
import toothpick.InjectConstructor

@InjectConstructor
class TorrentDbDataSource(
    private val dao: TorrentDao,
    private val converter: TorrentConverter
) {

    fun getListAll(): Single<List<Torrent>> = dao
        .getListAll()
        .map(converter::toDomain)

    fun getList(releaseIds: List<Int>): Single<List<Torrent>> = dao
        .getList(releaseIds)
        .map(converter::toDomain)

    fun getListByPairIds(ids: List<Pair<Int, Int>>): Single<List<Torrent>> = dao
        .getListByKeys(converter.toDbKey(ids))
        .map(converter::toDomain)

    fun getOne(releaseId: Int, torrentId: Int): Single<Torrent> = dao
        .getOne(releaseId, torrentId)
        .map(converter::toDomain)

    fun insert(items: List<Torrent>): Completable = Single.just(items)
        .map(converter::toDb)
        .flatMapCompletable(dao::insert)

    fun removeList(ids: List<Pair<Int, Int>>): Completable = dao.delete(converter.toDbKey(ids))

    fun deleteAll(): Completable = dao.deleteAll()
}