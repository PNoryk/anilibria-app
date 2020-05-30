package ru.radiationx.data.acache.impl

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import ru.radiationx.data.acache.EpisodeCache
import ru.radiationx.data.acache.common.flatMapIfListEmpty
import ru.radiationx.data.acache.memory.EpisodeMemoryDataSource
import ru.radiationx.data.adb.datasource.EpisodeDbDataSource
import ru.radiationx.data.adomain.entity.relative.FeedRelative
import ru.radiationx.data.adomain.entity.release.Episode
import toothpick.InjectConstructor

@InjectConstructor
class EpisodeCacheImpl(
    private val dbDataSource: EpisodeDbDataSource,
    private val memoryDataSource: EpisodeMemoryDataSource
) : EpisodeCache {

    override fun observeList(): Observable<List<Episode>> = memoryDataSource.observeListAll()

    override fun getList(): Single<List<Episode>> = memoryDataSource
        .getListAll()
        .flatMapIfListEmpty {
            dbDataSource
                .getListAll()
                .flatMapCompletable { memoryDataSource.insert(it) }
                .andThen(memoryDataSource.getListAll())
        }

    override fun getList(releaseId: Int): Single<List<Episode>> = memoryDataSource
        .getList(releaseId)
        .flatMapIfListEmpty {
            dbDataSource
                .getList(releaseId)
                .flatMapCompletable { memoryDataSource.insert(it) }
                .andThen(memoryDataSource.getList(releaseId))
        }

    override fun putList(items: List<Episode>): Completable = dbDataSource
        .insert(items)
        .andThen(dbDataSource.getList(items.toIds()))
        .flatMapCompletable { memoryDataSource.insert(it) }

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