package anilibria.tv.cache.impl

import anilibria.tv.cache.EpisodeCache
import anilibria.tv.cache.impl.common.flatMapIfListEmpty
import anilibria.tv.cache.impl.memory.EpisodeMemoryDataSource
import anilibria.tv.domain.entity.common.keys.EpisodeKey
import anilibria.tv.cache.impl.merger.EpisodeMerger
import anilibria.tv.db.EpisodeDbDataSource
import anilibria.tv.domain.entity.episode.Episode
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

    override fun observeSome(keys: List<EpisodeKey>): Observable<List<Episode>> = memoryDataSource.observeSome(keys)

    override fun getList(): Single<List<Episode>> = memoryDataSource
        .getList()
        .flatMapIfListEmpty {
            dbDataSource
                .getList()
                .flatMapCompletable { memoryDataSource.insert(it.toKeyValues()) }
                .andThen(memoryDataSource.getList())
        }

    override fun getSome(keys: List<EpisodeKey>): Single<List<Episode>> = memoryDataSource
        .getSome(keys)
        .flatMapIfListEmpty {
            dbDataSource
                .getSome(keys)
                .flatMapCompletable { memoryDataSource.insert(it.toKeyValues()) }
                .andThen(memoryDataSource.getSome(keys))
        }

    override fun putList(items: List<Episode>): Completable = getSome(items.toKeys())
        .map { episodeMerger.filterSame(it, items) }
        .flatMapCompletable { newItems ->
            if (newItems.isEmpty()) {
                return@flatMapCompletable Completable.complete()
            }
            dbDataSource
                .insert(newItems)
                .andThen(dbDataSource.getSome(newItems.toKeys()))
                .flatMapCompletable { memoryDataSource.insert(it.toKeyValues()) }
        }

    override fun removeList(keys: List<EpisodeKey>): Completable = dbDataSource
        .remove(keys)
        .andThen(memoryDataSource.removeList(keys))

    override fun clear(): Completable = dbDataSource
        .clear()
        .andThen(memoryDataSource.clear())

    private fun List<Episode>.toKeys() = map { EpisodeKey(it.releaseId, it.id) }

    private fun List<Episode>.toKeyValues() = map { Pair(EpisodeKey(it.releaseId, it.id), it) }
}