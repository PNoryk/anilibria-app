package anilibria.tv.cache.impl

import anilibria.tv.cache.TorrentCache
import anilibria.tv.cache.impl.common.flatMapIfListEmpty
import anilibria.tv.cache.impl.memory.TorrentMemoryDataSource
import anilibria.tv.cache.impl.memory.keys.EpisodeMemoryKey
import anilibria.tv.cache.impl.memory.keys.TorrentMemoryKey
import anilibria.tv.cache.impl.merger.TorrentMerger
import anilibria.tv.db.TorrentDbDataSource
import anilibria.tv.domain.entity.episode.Episode
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

    override fun observeList(releaseIds: List<Int>): Observable<List<Torrent>> = memoryDataSource.observeSome(releaseIds.toReleaseKeys())

    override fun getList(): Single<List<Torrent>> = memoryDataSource
        .getList()
        .flatMapIfListEmpty {
            dbDataSource
                .getListAll()
                .flatMapCompletable { memoryDataSource.insert(it.toKeyValues()) }
                .andThen(memoryDataSource.getList())
        }

    override fun getList(releaseIds: List<Int>): Single<List<Torrent>> = memoryDataSource
        .getSome(releaseIds.toReleaseKeys())
        .flatMapIfListEmpty {
            dbDataSource
                .getList(releaseIds)
                .flatMapCompletable { memoryDataSource.insert(it.toKeyValues()) }
                .andThen(memoryDataSource.getSome(releaseIds.toReleaseKeys()))
        }

    override fun putList(items: List<Torrent>): Completable = getList(items.map { it.releaseId })
        .map { torrentMerger.filterSame(it, items) }
        .flatMapCompletable { newItems ->
            if (newItems.isEmpty()) {
                return@flatMapCompletable Completable.complete()
            }
            dbDataSource
                .insert(newItems)
                .andThen(dbDataSource.getListByPairIds(newItems.toIds()))
                .flatMapCompletable { memoryDataSource.insert(it.toKeyValues()) }
        }

    override fun removeList(items: List<Torrent>): Completable = dbDataSource
        .removeList(items.toIds())
        .andThen(memoryDataSource.removeList(items.toKeys()))

    override fun clear(): Completable = dbDataSource
        .deleteAll()
        .andThen(memoryDataSource.clear())

    private fun List<Torrent>.toIds() = map { Pair(it.releaseId, it.id) }

    private fun List<Int>.toReleaseKeys() = map { TorrentMemoryKey(it, null) }

    private fun List<Torrent>.toKeys() = map { TorrentMemoryKey(it.releaseId, it.id) }

    private fun List<Torrent>.toKeyValues() = map { Pair(TorrentMemoryKey(it.releaseId, it.id), it) }
}