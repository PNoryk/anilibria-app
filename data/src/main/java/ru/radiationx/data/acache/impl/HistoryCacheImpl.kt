package ru.radiationx.data.acache.impl

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import anilibria.tv.cache.HistoryCache
import anilibria.tv.cache.impl.memory.HistoryMemoryDataSource
import ru.radiationx.data.adb.datasource.HistoryDbDataSource
import anilibria.tv.domain.entity.relative.HistoryRelative
import toothpick.InjectConstructor

@InjectConstructor
class HistoryCacheImpl(
    private val dbDataSource: HistoryDbDataSource,
    private val memoryDataSource: HistoryMemoryDataSource
) : HistoryCache {

    override fun observeList(): Observable<List<HistoryRelative>> = memoryDataSource.observeListAll()

    override fun getList(): Single<List<HistoryRelative>> = memoryDataSource
        .getListAll()
        .flatMapIfListEmpty {
            dbDataSource
                .getListAll()
                .flatMapCompletable { memoryDataSource.insert(it) }
                .andThen(memoryDataSource.getListAll())
        }

    override fun putList(items: List<HistoryRelative>): Completable = dbDataSource
        .insert(items)
        .andThen(dbDataSource.getList(items.toIds()))
        .flatMapCompletable { memoryDataSource.insert(it) }

    override fun removeList(items: List<HistoryRelative>): Completable {
        val ids = items.toIds()
        return dbDataSource
            .removeList(ids)
            .andThen(memoryDataSource.removeList(ids))
    }

    override fun clear(): Completable = dbDataSource
        .deleteAll()
        .andThen(memoryDataSource.deleteAll())

    private fun List<HistoryRelative>.toIds() = map { it.releaseId }
}