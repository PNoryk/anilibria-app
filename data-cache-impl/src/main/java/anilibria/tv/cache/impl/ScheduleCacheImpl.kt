package anilibria.tv.cache.impl

import anilibria.tv.cache.ScheduleCache
import anilibria.tv.cache.impl.common.flatMapIfListEmpty
import anilibria.tv.cache.impl.memory.ScheduleMemoryDataSource
import anilibria.tv.domain.entity.common.keys.ScheduleKey
import anilibria.tv.db.ScheduleDbDataSource
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
                .getList()
                .flatMapCompletable { memoryDataSource.insert(it.toKeyValues()) }
                .andThen(memoryDataSource.getList())
        }

    override fun insert(items: List<ScheduleDayRelative>): Completable = dbDataSource
        .insert(items)
        .andThen(dbDataSource.getSome(items.toKeys()))
        .flatMapCompletable { memoryDataSource.insert(it.toKeyValues()) }

    override fun remove(keys: List<ScheduleKey>): Completable = dbDataSource
        .remove(keys)
        .andThen(memoryDataSource.remove(keys))

    override fun clear(): Completable = dbDataSource
        .clear()
        .andThen(memoryDataSource.clear())

    private fun List<ScheduleDayRelative>.toKeys() = map { ScheduleKey(it.dayId) }

    private fun List<ScheduleDayRelative>.toKeyValues() = map { Pair(ScheduleKey(it.dayId), it) }
}