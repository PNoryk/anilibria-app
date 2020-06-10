package anilibria.tv.cache.impl

import anilibria.tv.cache.YoutubeCache
import anilibria.tv.cache.impl.common.flatMapIfListEmpty
import anilibria.tv.cache.impl.memory.YoutubeMemoryDataSource
import anilibria.tv.domain.entity.common.keys.YoutubeKey
import anilibria.tv.cache.impl.merger.YoutubeMerger
import anilibria.tv.db.YoutubeDbDataSource
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

    override fun observeSome(keys: List<YoutubeKey>): Observable<List<Youtube>> = memoryDataSource.observeSome(keys)

    override fun getList(): Single<List<Youtube>> = memoryDataSource
        .getList()
        .flatMapIfListEmpty {
            dbDataSource
                .getList()
                .flatMapCompletable { memoryDataSource.insert(it.toKeyValues()) }
                .andThen(memoryDataSource.getList())
        }

    override fun getSome(keys: List<YoutubeKey>): Single<List<Youtube>> = memoryDataSource
        .getSome(keys)
        .flatMapIfListEmpty {
            dbDataSource
                .getSome(keys)
                .flatMapCompletable { memoryDataSource.insert(it.toKeyValues()) }
                .andThen(memoryDataSource.getSome(keys))
        }

    override fun insert(items: List<Youtube>): Completable = getSome(items.toKeys())
        .map { youtubeMerger.filterSame(it, items) }
        .flatMapCompletable { newItems ->
            if (newItems.isEmpty()) {
                return@flatMapCompletable Completable.complete()
            }
            dbDataSource
                .insert(items)
                .andThen(dbDataSource.getSome(items.toKeys()))
                .flatMapCompletable { memoryDataSource.insert(it.toKeyValues()) }
        }

    override fun remove(keys: List<YoutubeKey>): Completable = dbDataSource
        .remove(keys)
        .andThen(memoryDataSource.remove(keys))

    override fun clear(): Completable = dbDataSource
        .clear()
        .andThen(memoryDataSource.clear())

    private fun List<Youtube>.toKeys() = map { YoutubeKey(it.id) }

    private fun List<Youtube>.toKeyValues() = map { Pair(YoutubeKey(it.id), it) }
}