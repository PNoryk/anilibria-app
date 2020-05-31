package anilibria.tv.cache.impl

import anilibria.tv.cache.FeedCache
import anilibria.tv.cache.impl.common.flatMapIfListEmpty
import anilibria.tv.cache.impl.memory.FeedMemoryDataSource
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

    override fun observeList(): Observable<List<FeedRelative>> = memoryDataSource.observeListAll()

    override fun getList(): Single<List<FeedRelative>> = memoryDataSource
        .getListAll()
        .flatMapIfListEmpty {
            dbDataSource
                .getListAll()
                .flatMapCompletable { memoryDataSource.insert(it) }
                .andThen(memoryDataSource.getListAll())
        }

    override fun putList(items: List<FeedRelative>): Completable = dbDataSource
        .insert(items)
        .andThen(dbDataSource.getList(items.toIds()))
        .flatMapCompletable { memoryDataSource.insert(it) }

    override fun removeList(items: List<FeedRelative>): Completable {
        val ids = items.toIds()
        return dbDataSource
            .removeList(ids)
            .andThen(memoryDataSource.removeList(ids))
    }

    override fun clear(): Completable = dbDataSource
        .deleteAll()
        .andThen(memoryDataSource.deleteAll())

    private fun List<FeedRelative>.toIds() = map { Pair(it.releaseId, it.youtubeId) }
}