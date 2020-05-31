package ru.radiationx.data.acache.impl

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import ru.radiationx.data.acache.YoutubeCache
import ru.radiationx.data.acache.common.flatMapIfListEmpty
import ru.radiationx.data.acache.memory.YoutubeMemoryDataSource
import ru.radiationx.data.acache.merger.YoutubeMerger
import ru.radiationx.data.adb.datasource.YoutubeDbDataSource
import anilibria.tv.domain.entity.youtube.Youtube
import toothpick.InjectConstructor

@InjectConstructor
class YoutubeCacheImpl(
    private val dbDataSource: YoutubeDbDataSource,
    private val memoryDataSource: YoutubeMemoryDataSource,
    private val youtubeMerger: YoutubeMerger
) : YoutubeCache {

    override fun observeList(): Observable<List<Youtube>> = memoryDataSource.observeListAll()

    override fun observeList(ids: List<Int>): Observable<List<Youtube>> = memoryDataSource.observeList(ids)

    override fun getList(): Single<List<Youtube>> = memoryDataSource
        .getListAll()
        .flatMapIfListEmpty {
            dbDataSource
                .getListAll()
                .flatMapCompletable { memoryDataSource.insert(it) }
                .andThen(memoryDataSource.getListAll())
        }

    override fun getList(ids: List<Int>): Single<List<Youtube>> = memoryDataSource
        .getList(ids)
        .flatMapIfListEmpty {
            dbDataSource
                .getList(ids)
                .flatMapCompletable { memoryDataSource.insert(it) }
                .andThen(memoryDataSource.getList(ids))
        }

    override fun putList(items: List<Youtube>): Completable = getList(items.toIds())
        .map { youtubeMerger.filterSame(it, items) }
        .flatMapCompletable { newItems ->
            if (newItems.isEmpty()) {
                return@flatMapCompletable Completable.complete()
            }
            dbDataSource
                .insert(items)
                .andThen(dbDataSource.getList(items.toIds()))
                .flatMapCompletable { memoryDataSource.insert(it) }
        }

    override fun removeList(items: List<Youtube>): Completable {
        val ids = items.toIds()
        return dbDataSource
            .removeList(ids)
            .andThen(memoryDataSource.removeList(ids))
    }

    override fun clear(): Completable = dbDataSource
        .deleteAll()
        .andThen(memoryDataSource.deleteAll())

    private fun List<Youtube>.toIds() = map { it.id }
}