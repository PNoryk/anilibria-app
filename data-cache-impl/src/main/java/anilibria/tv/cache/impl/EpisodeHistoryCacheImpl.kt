package anilibria.tv.cache.impl

import anilibria.tv.cache.EpisodeHistoryCache
import anilibria.tv.cache.impl.common.flatMapIfListEmpty
import anilibria.tv.cache.impl.memory.EpisodeHistoryMemoryDataSource
import anilibria.tv.domain.entity.common.keys.EpisodeKey
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

    override fun observeList(): Observable<List<EpisodeHistoryRelative>> = memoryDataSource.observeList()

    override fun observeSome(keys: List<EpisodeKey>): Observable<List<EpisodeHistoryRelative>> = memoryDataSource.observeSome(keys)

    override fun getList(): Single<List<EpisodeHistoryRelative>> = memoryDataSource
        .getList()
        .flatMapIfListEmpty {
            dbDataSource
                .getList()
                .flatMapCompletable { memoryDataSource.insert(it.toKeyValues()) }
                .andThen(memoryDataSource.getList())
        }

    override fun getSome(keys: List<EpisodeKey>): Single<List<EpisodeHistoryRelative>> = memoryDataSource
        .getSome(keys)
        .flatMapIfListEmpty {
            dbDataSource
                .getSome(keys)
                .flatMapCompletable { memoryDataSource.insert(it.toKeyValues()) }
                .andThen(memoryDataSource.getSome(keys))
        }

    override fun insert(items: List<EpisodeHistoryRelative>): Completable = dbDataSource
        .insert(items)
        .andThen(dbDataSource.getSome(items.toKeys()))
        .flatMapCompletable { memoryDataSource.insert(it.toKeyValues()) }

    override fun remove(keys: List<EpisodeKey>): Completable = dbDataSource
        .remove(keys)
        .andThen(memoryDataSource.remove(keys))

    override fun clear(): Completable = dbDataSource
        .clear()
        .andThen(memoryDataSource.clear())

    private fun List<EpisodeHistoryRelative>.toKeys() = map { EpisodeKey(it.releaseId, it.id) }

    private fun List<EpisodeHistoryRelative>.toKeyValues() = map { Pair(EpisodeKey(it.releaseId, it.id), it) }
}