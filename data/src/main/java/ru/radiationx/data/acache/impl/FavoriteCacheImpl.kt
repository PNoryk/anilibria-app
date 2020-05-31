package ru.radiationx.data.acache.impl

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import anilibria.tv.cache.FavoriteCache
import anilibria.tv.cache.impl.memory.FavoriteMemoryDataSource
import ru.radiationx.data.adb.datasource.FavoriteDbDataSource
import anilibria.tv.domain.entity.relative.FavoriteRelative
import toothpick.InjectConstructor

@InjectConstructor
class FavoriteCacheImpl(
    private val dbDataSource: FavoriteDbDataSource,
    private val memoryDataSource: FavoriteMemoryDataSource
) : FavoriteCache {

    override fun observeList(): Observable<List<FavoriteRelative>> = memoryDataSource.observeListAll()

    override fun getList(): Single<List<FavoriteRelative>> = memoryDataSource
        .getListAll()
        .flatMapIfListEmpty {
            dbDataSource
                .getListAll()
                .flatMapCompletable { memoryDataSource.insert(it) }
                .andThen(memoryDataSource.getListAll())
        }

    override fun putList(items: List<FavoriteRelative>): Completable = dbDataSource
        .insert(items)
        .andThen(dbDataSource.getList(items.toIds()))
        .flatMapCompletable { memoryDataSource.insert(it) }

    override fun removeList(items: List<FavoriteRelative>): Completable {
        val ids = items.toIds()
        return dbDataSource
            .removeList(ids)
            .andThen(memoryDataSource.removeList(ids))
    }

    override fun clear(): Completable = dbDataSource
        .deleteAll()
        .andThen(memoryDataSource.deleteAll())

    private fun List<FavoriteRelative>.toIds() = map { it.releaseId }
}