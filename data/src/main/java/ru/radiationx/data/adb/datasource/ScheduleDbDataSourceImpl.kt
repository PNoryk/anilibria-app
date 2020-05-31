package ru.radiationx.data.adb.datasource

import anilibria.tv.db.ScheduleDbDataSource
import anilibria.tv.domain.entity.relative.ScheduleDayRelative
import io.reactivex.Completable
import io.reactivex.Single
import ru.radiationx.data.adb.converters.ScheduleConverter
import ru.radiationx.data.adb.dao.ScheduleDao
import toothpick.InjectConstructor

@InjectConstructor
class ScheduleDbDataSourceImpl(
    private val dao: ScheduleDao,
    private val converter: ScheduleConverter
) : ScheduleDbDataSource {

    override fun getListAll(): Single<List<ScheduleDayRelative>> = dao
        .getListAll()
        .map(converter::toDomain)

    override fun getList(ids: List<Int>): Single<List<ScheduleDayRelative>> = dao
        .getList(ids)
        .map(converter::toDomain)

    override fun getOne(dayId: Int): Single<ScheduleDayRelative> = dao
        .getOne(dayId)
        .map(converter::toDomain)

    override fun insert(items: List<ScheduleDayRelative>): Completable = Single.just(items)
        .map(converter::toDb)
        .flatMapCompletable(dao::insert)

    override fun removeList(ids: List<Int>): Completable = dao.delete(ids)

    override fun deleteAll(): Completable = dao.deleteAll()
}