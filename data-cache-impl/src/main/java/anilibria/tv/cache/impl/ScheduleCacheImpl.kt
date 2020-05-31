package anilibria.tv.cache.impl

import anilibria.tv.cache.ScheduleCache
import anilibria.tv.cache.impl.common.flatMapIfListEmpty
import anilibria.tv.cache.impl.memory.ScheduleMemoryDataSource
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

    override fun observeList(): Observable<List<ScheduleDayRelative>> = memoryDataSource.observeListAll()

    override fun getList(): Single<List<ScheduleDayRelative>> = memoryDataSource
        .getListAll()
        .flatMapIfListEmpty {
            dbDataSource
                .getListAll()
                .flatMapCompletable { memoryDataSource.insert(it) }
                .andThen(memoryDataSource.getListAll())
        }

    override fun putList(items: List<ScheduleDayRelative>): Completable = dbDataSource
        .insert(items)
        .andThen(dbDataSource.getList(items.toIds()))
        .flatMapCompletable { memoryDataSource.insert(it) }

    override fun removeList(items: List<ScheduleDayRelative>): Completable {
        val ids = items.toIds()
        return dbDataSource
            .removeList(ids)
            .andThen(memoryDataSource.removeList(ids))
    }

    override fun clear(): Completable = dbDataSource
        .deleteAll()
        .andThen(memoryDataSource.deleteAll())

    private fun List<ScheduleDayRelative>.toIds() = map { it.dayId }
}