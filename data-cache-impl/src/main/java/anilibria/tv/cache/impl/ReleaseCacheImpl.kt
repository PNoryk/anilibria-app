package anilibria.tv.cache.impl

import anilibria.tv.cache.ReleaseCache
import anilibria.tv.cache.impl.common.flatMapIfListEmpty
import anilibria.tv.cache.impl.memory.ReleaseMemoryDataSource
import anilibria.tv.domain.entity.common.keys.ReleaseKey
import anilibria.tv.cache.impl.merger.ReleaseMerger
import anilibria.tv.db.ReleaseDbDataSource
import anilibria.tv.domain.entity.release.Release
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import toothpick.InjectConstructor

@InjectConstructor
class ReleaseCacheImpl(
    private val dbDataSource: ReleaseDbDataSource,
    private val memoryDataSource: ReleaseMemoryDataSource,
    private val releaseMerger: ReleaseMerger
) : ReleaseCache {

    override fun observeList(): Observable<List<Release>> = memoryDataSource.observeList()

    override fun observeSome(keys: List<ReleaseKey>): Observable<List<Release>> = memoryDataSource.observeSome(keys)

    override fun observeOne(key: ReleaseKey): Observable<Release> = memoryDataSource.observeOne(key)

    override fun getList(): Single<List<Release>> = memoryDataSource
        .getList()
        .flatMapIfListEmpty {
            dbDataSource
                .getList()
                .flatMapCompletable { memoryDataSource.insert(it.toKeyValues()) }
                .andThen(memoryDataSource.getList())
        }

    override fun getSome(keys: List<ReleaseKey>): Single<List<Release>> = memoryDataSource
        .getSome(keys)
        .flatMapIfListEmpty {
            dbDataSource
                .getSome(keys)
                .flatMapCompletable { memoryDataSource.insert(it.toKeyValues()) }
                .andThen(memoryDataSource.getSome(keys))
        }

    override fun getOne(key: ReleaseKey): Single<Release> = memoryDataSource
        .getOne(key)
        .onErrorResumeNext {
            dbDataSource
                .getOne(key)
                .flatMapCompletable { memoryDataSource.insert(listOf(it).toKeyValues()) }
                .andThen(memoryDataSource.getOne(key))
        }

    override fun putList(items: List<Release>): Completable = getSome(items.toKeys())
        .map { releaseMerger.filterSame(it, items) }
        .flatMapCompletable { newItems ->
            if (newItems.isEmpty()) {
                return@flatMapCompletable Completable.complete()
            }
            dbDataSource.insert(newItems)
                .andThen(dbDataSource.getSome(newItems.toKeys()))
                .flatMapCompletable { memoryDataSource.insert(it.toKeyValues()) }
        }

    override fun removeList(keys: List<ReleaseKey>): Completable = dbDataSource
        .remove(keys)
        .andThen(memoryDataSource.removeList(keys))

    override fun clear(): Completable = dbDataSource
        .clear()
        .andThen(memoryDataSource.clear())

    private fun List<Release>.toKeys() = map { ReleaseKey(it.id) }

    private fun List<Release>.toKeyValues() = map { Pair(ReleaseKey(it.id), it) }
}