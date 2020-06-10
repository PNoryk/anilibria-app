package anilibria.tv.cache.impl

import anilibria.tv.cache.ReleaseHistoryCache
import anilibria.tv.cache.impl.common.flatMapIfListEmpty
import anilibria.tv.cache.impl.memory.ReleaseHistoryMemoryDataSource
import anilibria.tv.domain.entity.common.keys.ReleaseKey
import anilibria.tv.db.ReleaseHistoryDbDataSource
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
                .getList()
                .flatMapCompletable { memoryDataSource.insert(it.toKeyValues()) }
                .andThen(memoryDataSource.getList())
        }

    override fun putList(items: List<ReleaseHistoryRelative>): Completable = dbDataSource
        .insert(items)
        .andThen(dbDataSource.getSome(items.toKeys()))
        .flatMapCompletable { memoryDataSource.insert(it.toKeyValues()) }

    override fun removeList(keys: List<ReleaseKey>): Completable = dbDataSource
        .remove(keys)
        .andThen(memoryDataSource.removeList(keys))

    override fun clear(): Completable = dbDataSource
        .clear()
        .andThen(memoryDataSource.clear())

    private fun List<ReleaseHistoryRelative>.toKeys() = map { ReleaseKey(it.releaseId) }

    private fun List<ReleaseHistoryRelative>.toKeyValues() = map { Pair(ReleaseKey(it.releaseId), it) }
}