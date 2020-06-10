package anilibria.tv.db

import anilibria.tv.domain.entity.common.keys.TorrentKey
import anilibria.tv.domain.entity.torrent.Torrent
import io.reactivex.Completable
import io.reactivex.Single

interface TorrentDbDataSource {
    fun getList(): Single<List<Torrent>>
    fun getSome(keys: List<TorrentKey>): Single<List<Torrent>>
    fun getOne(key: TorrentKey): Single<Torrent>
    fun insert(items: List<Torrent>): Completable
    fun remove(keys: List<TorrentKey>): Completable
    fun clear(): Completable
}