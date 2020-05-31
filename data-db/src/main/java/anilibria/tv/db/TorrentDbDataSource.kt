package anilibria.tv.db

import anilibria.tv.domain.entity.torrent.Torrent
import io.reactivex.Completable
import io.reactivex.Single

interface TorrentDbDataSource {
    fun getListAll(): Single<List<Torrent>>
    fun getList(releaseIds: List<Int>): Single<List<Torrent>>
    fun getListByPairIds(ids: List<Pair<Int, Int>>): Single<List<Torrent>>
    fun getOne(releaseId: Int, torrentId: Int): Single<Torrent>
    fun insert(items: List<Torrent>): Completable
    fun removeList(ids: List<Pair<Int, Int>>): Completable
    fun deleteAll(): Completable
}