package anilibria.tv.cache.impl

import anilibria.tv.cache.TorrentCache
import anilibria.tv.cache.impl.common.flatMapIfListEmpty
import anilibria.tv.cache.impl.memory.TorrentMemoryDataSource
import anilibria.tv.domain.entity.common.keys.TorrentKey
import anilibria.tv.cache.impl.merger.TorrentMerger
import anilibria.tv.db.TorrentDbDataSource
import anilibria.tv.domain.entity.torrent.Torrent
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import toothpick.InjectConstructor

@InjectConstructor
class TorrentCacheImpl(
    private val dbDataSource: TorrentDbDataSource,
    private val memoryDataSource: TorrentMemoryDataSource,
    private val torrentMerger: TorrentMerger
) : TorrentCache {

    override fun observeList(): Observable<List<Torrent>> = memoryDataSource.observeList()

    override fun observeSome(keys: List<TorrentKey>): Observable<List<Torrent>> = memoryDataSource.observeSome(keys)

    override fun getList(): Single<List<Torrent>> = memoryDataSource
        .getList()
        .flatMapIfListEmpty {
            dbDataSource
                .getList()
                .flatMapCompletable { memoryDataSource.insert(it.toKeyValues()) }
                .andThen(memoryDataSource.getList())
        }

    override fun getSome(keys: List<TorrentKey>): Single<List<Torrent>> = memoryDataSource
        .getSome(keys)
        .flatMapIfListEmpty {
            dbDataSource
                .getSome(keys)
                .flatMapCompletable { memoryDataSource.insert(it.toKeyValues()) }
                .andThen(memoryDataSource.getSome(keys))
        }

    override fun putList(items: List<Torrent>): Completable = getSome(items.toKeys())
        .map { torrentMerger.filterSame(it, items) }
        .flatMapCompletable { newItems ->
            if (newItems.isEmpty()) {
                return@flatMapCompletable Completable.complete()
            }
            dbDataSource
                .insert(newItems)
                .andThen(dbDataSource.getSome(newItems.toKeys()))
                .flatMapCompletable { memoryDataSource.insert(it.toKeyValues()) }
        }

    override fun removeList(keys: List<TorrentKey>): Completable = dbDataSource
        .remove(keys)
        .andThen(memoryDataSource.removeList(keys))

    override fun clear(): Completable = dbDataSource
        .clear()
        .andThen(memoryDataSource.clear())

    private fun List<Torrent>.toKeys() = map { TorrentKey(it.releaseId, it.id) }

    private fun List<Torrent>.toKeyValues() = map { Pair(TorrentKey(it.releaseId, it.id), it) }
}