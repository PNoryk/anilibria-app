package anilibria.tv.cache.impl

import anilibria.tv.cache.FavoriteCache
import anilibria.tv.cache.impl.common.flatMapIfListEmpty
import anilibria.tv.cache.impl.memory.FavoriteMemoryDataSource
import anilibria.tv.domain.entity.common.keys.ReleaseKey
import anilibria.tv.db.FavoriteDbDataSource
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
                .getList()
                .flatMapCompletable { memoryDataSource.insert(it.toKeyValues()) }
                .andThen(memoryDataSource.getList())
        }

    override fun insert(items: List<FavoriteRelative>): Completable = dbDataSource
        .insert(items)
        .andThen(dbDataSource.getSome(items.toKeys()))
        .flatMapCompletable { memoryDataSource.insert(it.toKeyValues()) }

    override fun remove(keys: List<ReleaseKey>): Completable = dbDataSource
        .remove(keys)
        .andThen(memoryDataSource.remove(keys))

    override fun clear(): Completable = dbDataSource
        .clear()
        .andThen(memoryDataSource.clear())

    private fun List<FavoriteRelative>.toKeys() = map { ReleaseKey(it.releaseId) }

    private fun List<FavoriteRelative>.toKeyValues() = map { Pair(ReleaseKey(it.releaseId), it) }
}