package ru.radiationx.data.acache.impl

import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import ru.radiationx.data.acache.EpisodeCache
import ru.radiationx.data.acache.common.flatMapIfListEmpty
import ru.radiationx.data.acache.memory.EpisodeMemoryDataSource
import ru.radiationx.data.adb.datasource.EpisodeDbDataSource
import ru.radiationx.data.adomain.entity.release.Episode
import toothpick.InjectConstructor

@InjectConstructor
class EpisodeCacheImpl(
    private val episodeDbDataSource: EpisodeDbDataSource,
    private val episodeMemoryDataSource: EpisodeMemoryDataSource
) : EpisodeCache {

    override fun observeList(): Observable<List<Episode>> = episodeMemoryDataSource.observeListAll()

    override fun getList(): Single<List<Episode>> = episodeMemoryDataSource
        .getListAll()
        .flatMapIfListEmpty {
            episodeDbDataSource
                .getListAll()
                .flatMapCompletable { episodeMemoryDataSource.insert(it) }
                .andThen(episodeMemoryDataSource.getListAll())
        }

    override fun getList(releaseId: Int): Single<List<Episode>> = episodeMemoryDataSource
        .getList(releaseId)
        .flatMapIfListEmpty {
            episodeDbDataSource
                .getList(releaseId)
                .flatMapCompletable { episodeMemoryDataSource.insert(it) }
                .andThen(episodeMemoryDataSource.getList(releaseId))
        }

    override fun putList(items: List<Episode>): Completable {
        val ids = items.map { Pair(it.releaseId, it.id) }
        return episodeDbDataSource
            .insert(items)
            .andThen(episodeDbDataSource.getList(ids))
            .flatMapCompletable { episodeMemoryDataSource.insert(it) }
    }

    override fun removeList(items: List<Episode>): Completable {
        val ids = items.map { Pair(it.releaseId, it.id) }
        return episodeDbDataSource
            .removeList(ids)
            .andThen(episodeMemoryDataSource.removeList(ids))
    }

    override fun clear(): Completable = episodeDbDataSource
        .delete()
        .andThen(episodeMemoryDataSource.delete())
}