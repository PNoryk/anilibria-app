package anilibria.tv.cache.impl

import anilibria.tv.cache.EpisodeHistoryCache
import anilibria.tv.cache.impl.common.flatMapIfListEmpty
import anilibria.tv.cache.impl.memory.EpisodeHistoryMemoryDataSource
import anilibria.tv.cache.impl.memory.keys.EpisodeHistoryMemoryKey
import anilibria.tv.cache.impl.memory.keys.EpisodeMemoryKey
import anilibria.tv.db.EpisodeHistoryDbDataSource
import anilibria.tv.domain.entity.episode.Episode
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

    override fun observeList(releaseIds: List<Int>): Observable<List<EpisodeHistoryRelative>> =
        memoryDataSource.observeSome(releaseIds.toReleaseKeys())

    override fun getList(): Single<List<EpisodeHistoryRelative>> = memoryDataSource
        .getList()
        .flatMapIfListEmpty {
            dbDataSource
                .getListAll()
                .flatMapCompletable { memoryDataSource.insert(it.toKeyValues()) }
                .andThen(memoryDataSource.getList())
        }

    override fun getList(releaseIds: List<Int>): Single<List<EpisodeHistoryRelative>> = memoryDataSource
        .getSome(releaseIds.toReleaseKeys())
        .flatMapIfListEmpty {
            dbDataSource
                .getList(releaseIds)
                .flatMapCompletable { memoryDataSource.insert(it.toKeyValues()) }
                .andThen(memoryDataSource.getSome(releaseIds.toReleaseKeys()))
        }

    override fun putList(items: List<EpisodeHistoryRelative>): Completable = dbDataSource
        .insert(items)
        .andThen(dbDataSource.getListByPairIds(items.toIds()))
        .flatMapCompletable { memoryDataSource.insert(it.toKeyValues()) }

    override fun removeList(items: List<EpisodeHistoryRelative>): Completable = dbDataSource
        .removeList(items.toIds())
        .andThen(memoryDataSource.removeList(items.toKeys()))

    override fun clear(): Completable = dbDataSource
        .deleteAll()
        .andThen(memoryDataSource.clear())

    private fun List<EpisodeHistoryRelative>.toIds() = map { Pair(it.releaseId, it.id) }

    private fun List<Int>.toReleaseKeys() = map { EpisodeHistoryMemoryKey(it, null) }

    private fun List<EpisodeHistoryRelative>.toKeys() = map { EpisodeHistoryMemoryKey(it.releaseId, it.id) }

    private fun List<EpisodeHistoryRelative>.toKeyValues() = map { Pair(EpisodeHistoryMemoryKey(it.releaseId, it.id), it) }
}