package ru.radiationx.data.acache.impl

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import anilibria.tv.cache.ReleaseCache
import anilibria.tv.cache.impl.memory.ReleaseMemoryDataSource
import ru.radiationx.data.adb.datasource.ReleaseDbDataSource
import anilibria.tv.cache.merger.ReleaseMerger
import anilibria.tv.domain.entity.release.Release
import toothpick.InjectConstructor

@InjectConstructor
class ReleaseCacheImpl(
    private val dbDataSource: ReleaseDbDataSource,
    private val memoryDataSource: ReleaseMemoryDataSource,
    private val releaseMerger: ReleaseMerger
) : ReleaseCache {

    override fun observeList(): Observable<List<Release>> = memoryDataSource.observeListAll()

    override fun observeList(ids: List<Int>?, codes: List<String>?): Observable<List<Release>> = memoryDataSource.observeList(ids, codes)

    override fun observeOne(releaseId: Int?, releaseCode: String?): Observable<Release> =
        memoryDataSource.observeOne(releaseId, releaseCode)

    override fun getList(): Single<List<Release>> = memoryDataSource
        .getListAll()
        .flatMapIfListEmpty {
            dbDataSource
                .getListAll()
                .flatMapCompletable { memoryDataSource.insert(it) }
                .andThen(memoryDataSource.getListAll())
        }

    override fun getList(ids: List<Int>?, codes: List<String>?): Single<List<Release>> = memoryDataSource
        .getList(ids, codes)
        .flatMapIfListEmpty {
            dbDataSource
                .getList(ids, codes)
                .flatMapCompletable { memoryDataSource.insert(it) }
                .andThen(memoryDataSource.getList(ids, codes))
        }

    override fun getOne(releaseId: Int?, releaseCode: String?): Single<Release> = memoryDataSource
        .getOne(releaseId, releaseCode)
        .onErrorResumeNext {
            dbDataSource
                .getOne(releaseId, releaseCode)
                .flatMapCompletable { memoryDataSource.insert(listOf(it)) }
                .andThen(memoryDataSource.getOne(releaseId, releaseCode))
        }

    override fun putList(items: List<Release>): Completable = getList(items.toIds())
        .map { releaseMerger.filterSame(it, items) }
        .flatMapCompletable { newItems ->
            if (newItems.isEmpty()) {
                return@flatMapCompletable Completable.complete()
            }
            dbDataSource.insert(newItems)
                .andThen(dbDataSource.getList(newItems.toIds(), null))
                .flatMapCompletable { memoryDataSource.insert(it) }
        }

    override fun removeList(items: List<Release>): Completable {
        val ids = items.toIds()
        return dbDataSource
            .removeList(ids)
            .andThen(memoryDataSource.removeList(ids))
    }

    override fun clear(): Completable = dbDataSource
        .deleteAll()
        .andThen(memoryDataSource.deleteAll())

    private fun List<Release>.toIds() = map { it.id }
}