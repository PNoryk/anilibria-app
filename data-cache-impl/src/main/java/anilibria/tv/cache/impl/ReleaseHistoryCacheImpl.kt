package anilibria.tv.cache.impl

import anilibria.tv.cache.ReleaseHistoryCache
import anilibria.tv.cache.impl.common.flatMapIfListEmpty
import anilibria.tv.cache.impl.memory.ReleaseHistoryMemoryDataSource
import anilibria.tv.cache.impl.memory.keys.EpisodeHistoryMemoryKey
import anilibria.tv.cache.impl.memory.keys.ReleaseHistoryMemoryKey
import anilibria.tv.db.ReleaseHistoryDbDataSource
import anilibria.tv.domain.entity.relative.EpisodeHistoryRelative
import anilibria.tv.domain.entity.relative.ReleaseHistoryRelative
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import toothpick.InjectConstructor

@InjectConstructor
class ReleaseHistoryCacheImpl(
    private val dbDataSource: ReleaseHistoryDbDataSource,
    private val memoryDataSource: ReleaseHistoryMemoryDataSource
) : ReleaseHistoryCache {

    override fun observeList(): Observable<List<ReleaseHistoryRelative>> = memoryDataSource.observeList()

    override fun getList(): Single<List<ReleaseHistoryRelative>> = memoryDataSource
        .getList()
        .flatMapIfListEmpty {
            dbDataSource
                .getListAll()
                .flatMapCompletable { memoryDataSource.insert(it.toKeyValues()) }
                .andThen(memoryDataSource.getList())
        }

    override fun putList(items: List<ReleaseHistoryRelative>): Completable = dbDataSource
        .insert(items)
        .andThen(dbDataSource.getList(items.toIds()))
        .flatMapCompletable { memoryDataSource.insert(it.toKeyValues()) }

    override fun removeList(items: List<ReleaseHistoryRelative>): Completable = dbDataSource
        .removeList(items.toIds())
        .andThen(memoryDataSource.removeList(items.toKeys()))

    override fun clear(): Completable = dbDataSource
        .deleteAll()
        .andThen(memoryDataSource.clear())

    private fun List<ReleaseHistoryRelative>.toIds() = map { it.releaseId }

    private fun List<ReleaseHistoryRelative>.toKeys() = map { ReleaseHistoryMemoryKey(it.releaseId) }

    private fun List<ReleaseHistoryRelative>.toKeyValues() = map { Pair(ReleaseHistoryMemoryKey(it.releaseId), it) }
}