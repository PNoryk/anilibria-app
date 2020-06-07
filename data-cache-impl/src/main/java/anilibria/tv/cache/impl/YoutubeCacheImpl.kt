package anilibria.tv.cache.impl

import anilibria.tv.cache.YoutubeCache
import anilibria.tv.cache.impl.common.flatMapIfListEmpty
import anilibria.tv.cache.impl.memory.YoutubeMemoryDataSource
import anilibria.tv.cache.impl.memory.keys.ReleaseMemoryKey
import anilibria.tv.cache.impl.memory.keys.TorrentMemoryKey
import anilibria.tv.cache.impl.memory.keys.YoutubeMemoryKey
import anilibria.tv.cache.impl.merger.YoutubeMerger
import anilibria.tv.db.YoutubeDbDataSource
import anilibria.tv.domain.entity.release.Release
import anilibria.tv.domain.entity.youtube.Youtube
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import toothpick.InjectConstructor

@InjectConstructor
class YoutubeCacheImpl(
    private val dbDataSource: YoutubeDbDataSource,
    private val memoryDataSource: YoutubeMemoryDataSource,
    private val youtubeMerger: YoutubeMerger
) : YoutubeCache {

    override fun observeList(): Observable<List<Youtube>> = memoryDataSource.observeList()

    override fun observeList(ids: List<Int>): Observable<List<Youtube>> = memoryDataSource.observeSome(ids.toIdKeys())

    override fun getList(): Single<List<Youtube>> = memoryDataSource
        .getList()
        .flatMapIfListEmpty {
            dbDataSource
                .getListAll()
                .flatMapCompletable { memoryDataSource.insert(it.toKeyValues()) }
                .andThen(memoryDataSource.getList())
        }

    override fun getList(ids: List<Int>): Single<List<Youtube>> = memoryDataSource
        .getSome(ids.toIdKeys())
        .flatMapIfListEmpty {
            dbDataSource
                .getList(ids)
                .flatMapCompletable { memoryDataSource.insert(it.toKeyValues()) }
                .andThen(memoryDataSource.getSome(ids.toIdKeys()))
        }

    override fun putList(items: List<Youtube>): Completable = getList(items.toIds())
        .map { youtubeMerger.filterSame(it, items) }
        .flatMapCompletable { newItems ->
            if (newItems.isEmpty()) {
                return@flatMapCompletable Completable.complete()
            }
            dbDataSource
                .insert(items)
                .andThen(dbDataSource.getList(items.toIds()))
                .flatMapCompletable { memoryDataSource.insert(it.toKeyValues()) }
        }

    override fun removeList(items: List<Youtube>): Completable = dbDataSource
        .removeList(items.toIds())
        .andThen(memoryDataSource.removeList(items.toKeys()))

    override fun clear(): Completable = dbDataSource
        .deleteAll()
        .andThen(memoryDataSource.clear())

    private fun List<Youtube>.toIds() = map { it.id }

    private fun List<Int>.toIdKeys() = map { YoutubeMemoryKey(it) }

    private fun List<Youtube>.toKeys() = map { YoutubeMemoryKey(it.id) }

    private fun List<Youtube>.toKeyValues() = map { Pair(YoutubeMemoryKey(it.id), it) }
}