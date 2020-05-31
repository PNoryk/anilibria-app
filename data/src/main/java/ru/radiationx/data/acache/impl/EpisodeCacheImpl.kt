package ru.radiationx.data.acache.impl

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import ru.radiationx.data.acache.EpisodeCache
import ru.radiationx.data.acache.common.flatMapIfListEmpty
import ru.radiationx.data.acache.memory.EpisodeMemoryDataSource
import ru.radiationx.data.acache.merger.EpisodeMerger
import ru.radiationx.data.adb.datasource.EpisodeDbDataSource
import anilibria.tv.domain.entity.release.Episode
import toothpick.InjectConstructor

@InjectConstructor
class EpisodeCacheImpl(
    private val dbDataSource: EpisodeDbDataSource,
    private val memoryDataSource: EpisodeMemoryDataSource,
    private val episodeMerger: EpisodeMerger
) : EpisodeCache {

    override fun observeList(): Observable<List<Episode>> = memoryDataSource.observeListAll()

    override fun observeList(releaseIds: List<Int>): Observable<List<Episode>> = memoryDataSource.observeList(releaseIds)

    override fun getList(): Single<List<Episode>> = memoryDataSource
        .getListAll()
        .flatMapIfListEmpty {
            dbDataSource
                .getListAll()
                .flatMapCompletable { memoryDataSource.insert(it) }
                .andThen(memoryDataSource.getListAll())
        }

    override fun getList(releaseIds: List<Int>): Single<List<Episode>> = memoryDataSource
        .getList(releaseIds)
        .flatMapIfListEmpty {
            dbDataSource
                .getList(releaseIds)
                .flatMapCompletable { memoryDataSource.insert(it) }
                .andThen(memoryDataSource.getList(releaseIds))
        }

    override fun putList(items: List<Episode>): Completable = getList(items.map { it.releaseId })
        .map { episodeMerger.filterSame(it, items) }
        .flatMapCompletable { newItems ->
            if (newItems.isEmpty()) {
                return@flatMapCompletable Completable.complete()
            }
            dbDataSource
                .insert(items)
                .andThen(dbDataSource.getListByPairIds(items.toIds()))
                .flatMapCompletable { memoryDataSource.insert(it) }
        }

    override fun removeList(items: List<Episode>): Completable {
        val ids = items.toIds()
        return dbDataSource
            .removeList(ids)
            .andThen(memoryDataSource.removeList(ids))
    }

    override fun clear(): Completable = dbDataSource
        .deleteAll()
        .andThen(memoryDataSource.deleteAll())

    private fun List<Episode>.toIds() = map { Pair(it.releaseId, it.id) }
}