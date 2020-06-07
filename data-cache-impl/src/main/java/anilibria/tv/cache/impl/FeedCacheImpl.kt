package anilibria.tv.cache.impl

import anilibria.tv.cache.FeedCache
import anilibria.tv.cache.impl.common.flatMapIfListEmpty
import anilibria.tv.cache.impl.memory.FeedMemoryDataSource
import anilibria.tv.cache.impl.memory.keys.FeedMemoryKey
import anilibria.tv.db.FeedDbDataSource
import anilibria.tv.domain.entity.relative.FeedRelative
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import toothpick.InjectConstructor

@InjectConstructor
class FeedCacheImpl(
    private val dbDataSource: FeedDbDataSource,
    private val memoryDataSource: FeedMemoryDataSource
) : FeedCache {

    override fun observeList(): Observable<List<FeedRelative>> = memoryDataSource.observeList()

    override fun getList(): Single<List<FeedRelative>> = memoryDataSource
        .getList()
        .flatMapIfListEmpty {
            dbDataSource
                .getListAll()
                .flatMapCompletable { memoryDataSource.insert(it.toKeyValues()) }
                .andThen(memoryDataSource.getList())
        }

    override fun putList(items: List<FeedRelative>): Completable = dbDataSource
        .insert(items)
        .andThen(dbDataSource.getList(items.toIds()))
        .flatMapCompletable { memoryDataSource.insert(it.toKeyValues()) }

    override fun removeList(items: List<FeedRelative>): Completable = dbDataSource
        .removeList(items.toIds())
        .andThen(memoryDataSource.removeList(items.toKeys()))

    override fun clear(): Completable = dbDataSource
        .deleteAll()
        .andThen(memoryDataSource.clear())

    private fun List<FeedRelative>.toIds() = map { Pair(it.releaseId, it.youtubeId) }

    private fun List<FeedRelative>.toKeys() = map { FeedMemoryKey(it.releaseId, it.youtubeId) }

    private fun List<FeedRelative>.toKeyValues() = map { Pair(FeedMemoryKey(it.releaseId, it.youtubeId), it) }
}