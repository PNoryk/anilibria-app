package anilibria.tv.cache.impl

import anilibria.tv.cache.ReleaseCache
import anilibria.tv.cache.impl.common.flatMapIfListEmpty
import anilibria.tv.cache.impl.memory.ReleaseMemoryDataSource
import anilibria.tv.cache.impl.memory.keys.EpisodeMemoryKey
import anilibria.tv.cache.impl.memory.keys.ReleaseMemoryKey
import anilibria.tv.cache.impl.merger.ReleaseMerger
import anilibria.tv.db.ReleaseDbDataSource
import anilibria.tv.domain.entity.episode.Episode
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

    override fun observeList(ids: List<Int>?, codes: List<String>?): Observable<List<Release>> =
        memoryDataSource.observeSome(getKeyValues(ids, codes))

    override fun observeOne(releaseId: Int?, releaseCode: String?): Observable<Release> =
        memoryDataSource.observeOne(ReleaseMemoryKey(releaseId, releaseCode))

    override fun getList(): Single<List<Release>> = memoryDataSource
        .getList()
        .flatMapIfListEmpty {
            dbDataSource
                .getListAll()
                .flatMapCompletable { memoryDataSource.insert(it.toKeyValues()) }
                .andThen(memoryDataSource.getList())
        }

    override fun getList(ids: List<Int>?, codes: List<String>?): Single<List<Release>> = memoryDataSource
        .getSome(getKeyValues(ids, codes))
        .flatMapIfListEmpty {
            dbDataSource
                .getList(ids, codes)
                .flatMapCompletable { memoryDataSource.insert(it.toKeyValues()) }
                .andThen(memoryDataSource.getSome(getKeyValues(ids, codes)))
        }

    override fun getOne(releaseId: Int?, releaseCode: String?): Single<Release> = memoryDataSource
        .getOne(ReleaseMemoryKey(releaseId, releaseCode))
        .onErrorResumeNext {
            dbDataSource
                .getOne(releaseId, releaseCode)
                .flatMapCompletable { memoryDataSource.insert(listOf(it).toKeyValues()) }
                .andThen(memoryDataSource.getOne(ReleaseMemoryKey(releaseId, releaseCode)))
        }

    override fun putList(items: List<Release>): Completable = getList(items.toIds())
        .map { releaseMerger.filterSame(it, items) }
        .flatMapCompletable { newItems ->
            if (newItems.isEmpty()) {
                return@flatMapCompletable Completable.complete()
            }
            dbDataSource.insert(newItems)
                .andThen(dbDataSource.getList(newItems.toIds(), null))
                .flatMapCompletable { memoryDataSource.insert(it.toKeyValues()) }
        }

    override fun removeList(items: List<Release>): Completable = dbDataSource
        .removeList(items.toIds())
        .andThen(memoryDataSource.removeList(items.toKeys()))

    override fun clear(): Completable = dbDataSource
        .deleteAll()
        .andThen(memoryDataSource.clear())

    private fun List<Release>.toIds() = map { it.id }

    private fun List<Release>.toKeys() = map { ReleaseMemoryKey(it.id, it.code) }

    private fun List<Release>.toKeyValues() = map { Pair(ReleaseMemoryKey(it.id, it.code), it) }

    private fun getKeyValues(ids: List<Int>?, codes: List<String>?): List<ReleaseMemoryKey> {
        val result = mutableListOf<ReleaseMemoryKey>()
        ids?.map { ReleaseMemoryKey(it, null) }?.also { result.addAll(it) }
        codes?.map { ReleaseMemoryKey(null, it) }?.also { result.addAll(it) }
        return result
    }
}