package ru.radiationx.data.acache.impl

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import ru.radiationx.data.acache.TorrentCache
import ru.radiationx.data.acache.common.flatMapIfListEmpty
import ru.radiationx.data.acache.memory.TorrentMemoryDataSource
import ru.radiationx.data.adb.datasource.TorrentDbDataSource
import ru.radiationx.data.adomain.entity.release.Torrent
import toothpick.InjectConstructor

@InjectConstructor
class TorrentCacheImpl(
    private val dbDataSource: TorrentDbDataSource,
    private val memoryDataSource: TorrentMemoryDataSource
) : TorrentCache {

    override fun observeList(): Observable<List<Torrent>> = memoryDataSource.observeListAll()

    override fun observeList(releaseIds: List<Int>): Observable<List<Torrent>> = memoryDataSource.observeList(releaseIds)

    override fun getList(): Single<List<Torrent>> = memoryDataSource
        .getListAll()
        .flatMapIfListEmpty {
            dbDataSource
                .getListAll()
                .flatMapCompletable { memoryDataSource.insert(it) }
                .andThen(memoryDataSource.getListAll())
        }

    override fun getList(releaseIds: List<Int>): Single<List<Torrent>> = memoryDataSource
        .getList(releaseIds)
        .flatMapIfListEmpty {
            dbDataSource
                .getList(releaseIds)
                .flatMapCompletable { memoryDataSource.insert(it) }
                .andThen(memoryDataSource.getList(releaseIds))
        }

    override fun putList(items: List<Torrent>): Completable = dbDataSource
        .insert(items)
        .andThen(dbDataSource.getListByPairIds(items.toIds()))
        .flatMapCompletable { memoryDataSource.insert(it) }

    override fun removeList(items: List<Torrent>): Completable {
        val ids = items.toIds()
        return dbDataSource
            .removeList(ids)
            .andThen(memoryDataSource.removeList(ids))
    }

    override fun clear(): Completable = dbDataSource
        .deleteAll()
        .andThen(memoryDataSource.deleteAll())

    private fun List<Torrent>.toIds() = map { Pair(it.releaseId, it.id) }
}