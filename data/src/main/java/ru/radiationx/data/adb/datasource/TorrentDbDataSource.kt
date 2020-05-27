package ru.radiationx.data.adb.datasource

import io.reactivex.Completable
import io.reactivex.Single
import ru.radiationx.data.adb.dao.TorrentDao
import ru.radiationx.data.adb.converters.TorrentConverter
import ru.radiationx.data.adomain.release.Torrent
import toothpick.InjectConstructor

@InjectConstructor
class TorrentDbDataSource(
    private val torrentDao: TorrentDao,
    private val torrentConverter: TorrentConverter
) {

    fun getListAll(): Single<List<Torrent>> = torrentDao
        .getListAll()
        .map(torrentConverter::toDomain)

    fun getList(releaseId: Int): Single<List<Torrent>> = torrentDao
        .getList(releaseId)
        .map(torrentConverter::toDomain)

    fun getOne(releaseId: Int, torrentId: Int): Single<Torrent> = torrentDao
        .getOne(releaseId, torrentId)
        .map(torrentConverter::toDomain)

    fun insert(items: List<Pair<Int, Torrent>>): Completable = Single.just(items)
        .map(torrentConverter::toDb)
        .flatMapCompletable(torrentDao::insert)

    fun delete(): Completable = torrentDao.deleteAll()
}