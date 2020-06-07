package anilibria.tv.cache.impl

import anilibria.tv.cache.ScheduleCache
import anilibria.tv.cache.impl.common.flatMapIfListEmpty
import anilibria.tv.cache.impl.memory.ScheduleMemoryDataSource
import anilibria.tv.cache.impl.memory.keys.ReleaseHistoryMemoryKey
import anilibria.tv.cache.impl.memory.keys.ScheduleMemoryKey
import anilibria.tv.db.ScheduleDbDataSource
import anilibria.tv.domain.entity.relative.ReleaseHistoryRelative
import anilibria.tv.domain.entity.relative.ScheduleDayRelative
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import toothpick.InjectConstructor

@InjectConstructor
class ScheduleCacheImpl(
    private val dbDataSource: ScheduleDbDataSource,
    private val memoryDataSource: ScheduleMemoryDataSource
) : ScheduleCache {

    override fun observeList(): Observable<List<ScheduleDayRelative>> = memoryDataSource.observeList()

    override fun getList(): Single<List<ScheduleDayRelative>> = memoryDataSource
        .getList()
        .flatMapIfListEmpty {
            dbDataSource
                .getListAll()
                .flatMapCompletable { memoryDataSource.insert(it.toKeyValues()) }
                .andThen(memoryDataSource.getList())
        }

    override fun putList(items: List<ScheduleDayRelative>): Completable = dbDataSource
        .insert(items)
        .andThen(dbDataSource.getList(items.toIds()))
        .flatMapCompletable { memoryDataSource.insert(it.toKeyValues()) }

    override fun removeList(items: List<ScheduleDayRelative>): Completable = dbDataSource
        .removeList(items.toIds())
        .andThen(memoryDataSource.removeList(items.toKeys()))

    override fun clear(): Completable = dbDataSource
        .deleteAll()
        .andThen(memoryDataSource.clear())

    private fun List<ScheduleDayRelative>.toIds() = map { it.dayId }

    private fun List<ScheduleDayRelative>.toKeys() = map { ScheduleMemoryKey(it.dayId) }

    private fun List<ScheduleDayRelative>.toKeyValues() = map { Pair(ScheduleMemoryKey(it.dayId), it) }
}