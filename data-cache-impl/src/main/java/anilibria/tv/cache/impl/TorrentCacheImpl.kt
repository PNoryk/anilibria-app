package anilibria.tv.cache.impl

import anilibria.tv.cache.TorrentCache
import anilibria.tv.cache.impl.common.flatMapIfListEmpty
import anilibria.tv.cache.impl.memory.TorrentMemoryDataSource
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

    override fun observeList(): Observable<List<Torrent>> = memoryDataSource.observeListAll()

    override fun observeList(releaseIds: List<Int>): Observable<List<Torrent>> = memoryDataSource.observeList(releaseIds)

    override fun getList(): Single<List<Torrent>> = memoryDataSource
        .getListAll()
        .flatMapIfListEmpty {
            dbDataSource
                .getListAll()
                .flatMapCompletable { memoryDataSource.insert(it) }
                .andThen(memoryDataSource.getListAll())
        }

    override fun getList(releaseIds: List<Int>): Single<List<Torrent>> = memoryDataSource
        .getList(releaseIds)
        .flatMapIfListEmpty {
            dbDataSource
                .getList(releaseIds)
                .flatMapCompletable { memoryDataSource.insert(it) }
                .andThen(memoryDataSource.getList(releaseIds))
        }

    override fun putList(items: List<Torrent>): Completable = getList(items.map { it.releaseId })
        .map { torrentMerger.filterSame(it, items) }
        .flatMapCompletable { newItems ->
            if (newItems.isEmpty()) {
                return@flatMapCompletable Completable.complete()
            }
            dbDataSource
                .insert(items)
                .andThen(dbDataSource.getListByPairIds(items.toIds()))
                .flatMapCompletable { memoryDataSource.insert(it) }
        }

    override fun removeList(items: List<Torrent>): Completable {
        val ids = items.toIds()
        return dbDataSource
            .removeList(ids)
            .andThen(memoryDataSource.removeList(ids))
    }

    override fun clear(): Completable = dbDataSource
        .deleteAll()
        .andThen(memoryDataSource.deleteAll())

    private fun List<Torrent>.toIds() = map { Pair(it.releaseId, it.id) }
}