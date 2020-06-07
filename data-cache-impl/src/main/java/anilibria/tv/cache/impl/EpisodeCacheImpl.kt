package anilibria.tv.cache.impl

import anilibria.tv.cache.EpisodeCache
import anilibria.tv.cache.impl.common.flatMapIfListEmpty
import anilibria.tv.cache.impl.memory.EpisodeMemoryDataSource
import anilibria.tv.cache.impl.memory.keys.EpisodeMemoryKey
import anilibria.tv.cache.impl.memory.keys.FeedMemoryKey
import anilibria.tv.cache.impl.merger.EpisodeMerger
import anilibria.tv.db.EpisodeDbDataSource
import anilibria.tv.domain.entity.episode.Episode
import anilibria.tv.domain.entity.relative.FeedRelative
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import toothpick.InjectConstructor

@InjectConstructor
class EpisodeCacheImpl(
    private val dbDataSource: EpisodeDbDataSource,
    private val memoryDataSource: EpisodeMemoryDataSource,
    private val episodeMerger: EpisodeMerger
) : EpisodeCache {

    override fun observeList(): Observable<List<Episode>> = memoryDataSource.observeList()

    override fun observeList(releaseIds: List<Int>): Observable<List<Episode>> = memoryDataSource.observeSome(releaseIds.toReleaseKeys())

    override fun getList(): Single<List<Episode>> = memoryDataSource
        .getList()
        .flatMapIfListEmpty {
            dbDataSource
                .getListAll()
                .flatMapCompletable { memoryDataSource.insert(it.toKeyValues()) }
                .andThen(memoryDataSource.getList())
        }

    override fun getList(releaseIds: List<Int>): Single<List<Episode>> = memoryDataSource
        .getSome(releaseIds.toReleaseKeys())
        .flatMapIfListEmpty {
            dbDataSource
                .getList(releaseIds)
                .flatMapCompletable { memoryDataSource.insert(it.toKeyValues()) }
                .andThen(memoryDataSource.getSome(releaseIds.toReleaseKeys()))
        }

    override fun putList(items: List<Episode>): Completable = getList(items.map { it.releaseId })
        .map { episodeMerger.filterSame(it, items) }
        .flatMapCompletable { newItems ->
            if (newItems.isEmpty()) {
                return@flatMapCompletable Completable.complete()
            }
            dbDataSource
                .insert(newItems)
                .andThen(dbDataSource.getListByPairIds(newItems.toIds()))
                .flatMapCompletable { memoryDataSource.insert(it.toKeyValues()) }
        }

    override fun removeList(items: List<Episode>): Completable = dbDataSource
        .removeList(items.toIds())
        .andThen(memoryDataSource.removeList(items.toKeys()))

    override fun clear(): Completable = dbDataSource
        .deleteAll()
        .andThen(memoryDataSource.clear())

    private fun List<Episode>.toIds() = map { Pair(it.releaseId, it.id) }

    private fun List<Int>.toReleaseKeys() = map { EpisodeMemoryKey(it, null) }

    private fun List<Episode>.toKeys() = map { EpisodeMemoryKey(it.releaseId, it.id) }

    private fun List<Episode>.toKeyValues() = map { Pair(EpisodeMemoryKey(it.releaseId, it.id), it) }
}