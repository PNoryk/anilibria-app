package anilibria.tv.cache.impl

import anilibria.tv.cache.FavoriteCache
import anilibria.tv.cache.impl.common.flatMapIfListEmpty
import anilibria.tv.cache.impl.memory.FavoriteMemoryDataSource
import anilibria.tv.cache.impl.memory.keys.EpisodeMemoryKey
import anilibria.tv.cache.impl.memory.keys.FavoriteMemoryKey
import anilibria.tv.db.FavoriteDbDataSource
import anilibria.tv.domain.entity.relative.EpisodeHistoryRelative
import anilibria.tv.domain.entity.relative.FavoriteRelative
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import toothpick.InjectConstructor

@InjectConstructor
class FavoriteCacheImpl(
    private val dbDataSource: FavoriteDbDataSource,
    private val memoryDataSource: FavoriteMemoryDataSource
) : FavoriteCache {

    override fun observeList(): Observable<List<FavoriteRelative>> = memoryDataSource.observeList()

    override fun getList(): Single<List<FavoriteRelative>> = memoryDataSource
        .getList()
        .flatMapIfListEmpty {
            dbDataSource
                .getListAll()
                .flatMapCompletable { memoryDataSource.insert(it.toKeyValues()) }
                .andThen(memoryDataSource.getList())
        }

    override fun putList(items: List<FavoriteRelative>): Completable = dbDataSource
        .insert(items)
        .andThen(dbDataSource.getList(items.toIds()))
        .flatMapCompletable { memoryDataSource.insert(it.toKeyValues()) }

    override fun removeList(items: List<FavoriteRelative>): Completable = dbDataSource
        .removeList(items.toIds())
        .andThen(memoryDataSource.removeList(items.toKeys()))

    override fun clear(): Completable = dbDataSource
        .deleteAll()
        .andThen(memoryDataSource.clear())

    private fun List<FavoriteRelative>.toIds() = map { it.releaseId }

    private fun List<Int>.toReleaseKeys() = map { FavoriteMemoryKey(it) }

    private fun List<FavoriteRelative>.toKeys() = map { FavoriteMemoryKey(it.releaseId) }

    private fun List<FavoriteRelative>.toKeyValues() = map { Pair(FavoriteMemoryKey(it.releaseId), it) }
}