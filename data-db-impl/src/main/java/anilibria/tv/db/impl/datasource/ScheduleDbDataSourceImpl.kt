package anilibria.tv.db.impl.datasource

import anilibria.tv.db.ScheduleDbDataSource
import anilibria.tv.domain.entity.relative.ScheduleDayRelative
import io.reactivex.Completable
import io.reactivex.Single
import anilibria.tv.db.impl.converters.ScheduleConverter
import anilibria.tv.db.impl.dao.ScheduleDao
import anilibria.tv.domain.entity.common.keys.ScheduleKey
import toothpick.InjectConstructor

@InjectConstructor
class ScheduleDbDataSourceImpl(
    private val dao: ScheduleDao,
    private val converter: ScheduleConverter
) : ScheduleDbDataSource {

    override fun getList(): Single<List<ScheduleDayRelative>> = dao
        .getList()
        .map(converter::toDomain)

    override fun getSome(keys: List<ScheduleKey>): Single<List<ScheduleDayRelative>> = dao
        .getSome(keys.map { it.dayId })
        .map(converter::toDomain)

    override fun getOne(key: ScheduleKey): Single<ScheduleDayRelative> = dao
        .getOne(key.dayId)
        .map(converter::toDomain)

    override fun insert(items: List<ScheduleDayRelative>): Completable = Single.just(items)
        .map(converter::toDb)
        .flatMapCompletable(dao::insert)

    override fun remove(keys: List<ScheduleKey>): Completable = dao.remove(keys.map { it.dayId })

    override fun clear(): Completable = dao.clear()
}