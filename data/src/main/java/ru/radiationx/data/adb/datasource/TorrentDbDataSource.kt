package ru.radiationx.data.adb.datasource

import io.reactivex.Completable
import io.reactivex.Single
import ru.radiationx.data.adb.dao.TorrentDao
import ru.radiationx.data.adb.converters.TorrentConverter
import ru.radiationx.data.adomain.entity.release.Episode
import ru.radiationx.data.adomain.entity.release.Torrent
import toothpick.InjectConstructor

@InjectConstructor
class TorrentDbDataSource(
    private val dao: TorrentDao,
    private val converter: TorrentConverter
) {

    fun getListAll(): Single<List<Torrent>> = dao
        .getListAll()
        .map(converter::toDomain)

    fun getList(releaseId: Int): Single<List<Torrent>> = dao
        .getList(releaseId)
        .map(converter::toDomain)

    fun getList(ids: List<Pair<Int, Int>>): Single<List<Torrent>> = dao
        .getList(converter.toDbKey(ids))
        .map(converter::toDomain)

    fun getOne(releaseId: Int, torrentId: Int): Single<Torrent> = dao
        .getOne(releaseId, torrentId)
        .map(converter::toDomain)

    fun insert(items: List<Torrent>): Completable = Single.just(items)
        .map(converter::toDb)
        .flatMapCompletable(dao::insert)

    fun removeList(ids: List<Pair<Int, Int>>): Completable = dao.delete(converter.toDbKey(ids))

    fun delete(): Completable = dao.deleteAll()
}