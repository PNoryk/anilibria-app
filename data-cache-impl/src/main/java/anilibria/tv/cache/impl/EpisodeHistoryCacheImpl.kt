package anilibria.tv.cache.impl

import anilibria.tv.cache.EpisodeHistoryCache
import anilibria.tv.cache.impl.common.flatMapIfListEmpty
import anilibria.tv.cache.impl.memory.EpisodeHistoryMemoryDataSource
import anilibria.tv.db.EpisodeHistoryDbDataSource
import anilibria.tv.domain.entity.relative.EpisodeHistoryRelative
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import toothpick.InjectConstructor

@InjectConstructor
class EpisodeHistoryCacheImpl(
    private val dbDataSource: EpisodeHistoryDbDataSource,
    private val memoryDataSource: EpisodeHistoryMemoryDataSource
) : EpisodeHistoryCache {

    override fun observeList(): Observable<List<EpisodeHistoryRelative>> = memoryDataSource.observeListAll()

    override fun observeList(releaseIds: List<Int>): Observable<List<EpisodeHistoryRelative>> = memoryDataSource.observeList(releaseIds)

    override fun getList(): Single<List<EpisodeHistoryRelative>> = memoryDataSource
        .getListAll()
        .flatMapIfListEmpty {
            dbDataSource
                .getListAll()
                .flatMapCompletable { memoryDataSource.insert(it) }
                .andThen(memoryDataSource.getListAll())
        }

    override fun getList(releaseIds: List<Int>): Single<List<EpisodeHistoryRelative>> = memoryDataSource
        .getList(releaseIds)
        .flatMapIfListEmpty {
            dbDataSource
                .getList(releaseIds)
                .flatMapCompletable { memoryDataSource.insert(it) }
                .andThen(memoryDataSource.getList(releaseIds))
        }

    override fun putList(items: List<EpisodeHistoryRelative>): Completable = dbDataSource
        .insert(items)
        .andThen(dbDataSource.getListByPairIds(items.toIds()))
        .flatMapCompletable { memoryDataSource.insert(it) }

    override fun removeList(items: List<EpisodeHistoryRelative>): Completable {
        val ids = items.toIds()
        return dbDataSource
            .removeList(ids)
            .andThen(memoryDataSource.removeList(ids))
    }

    override fun clear(): Completable = dbDataSource
        .deleteAll()
        .andThen(memoryDataSource.deleteAll())

    private fun List<EpisodeHistoryRelative>.toIds() = map { Pair(it.releaseId, it.id) }
}