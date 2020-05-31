package anilibria.tv.cache.impl

import anilibria.tv.cache.ReleaseHistoryCache
import anilibria.tv.cache.impl.common.flatMapIfListEmpty
import anilibria.tv.cache.impl.memory.ReleaseHistoryMemoryDataSource
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

    override fun observeList(): Observable<List<ReleaseHistoryRelative>> = memoryDataSource.observeListAll()

    override fun getList(): Single<List<ReleaseHistoryRelative>> = memoryDataSource
        .getListAll()
        .flatMapIfListEmpty {
            dbDataSource
                .getListAll()
                .flatMapCompletable { memoryDataSource.insert(it) }
                .andThen(memoryDataSource.getListAll())
        }

    override fun putList(items: List<ReleaseHistoryRelative>): Completable = dbDataSource
        .insert(items)
        .andThen(dbDataSource.getList(items.toIds()))
        .flatMapCompletable { memoryDataSource.insert(it) }

    override fun removeList(items: List<ReleaseHistoryRelative>): Completable {
        val ids = items.toIds()
        return dbDataSource
            .removeList(ids)
            .andThen(memoryDataSource.removeList(ids))
    }

    override fun clear(): Completable = dbDataSource
        .deleteAll()
        .andThen(memoryDataSource.deleteAll())

    private fun List<ReleaseHistoryRelative>.toIds() = map { it.releaseId }
}