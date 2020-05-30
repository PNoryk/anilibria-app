package ru.radiationx.data.adb.datasource

import io.reactivex.Completable
import io.reactivex.Single
import ru.radiationx.data.adb.dao.ScheduleDao
import ru.radiationx.data.adb.converters.ScheduleConverter
import ru.radiationx.data.adomain.entity.relative.ScheduleDayRelative
import ru.radiationx.data.adomain.entity.release.Release
import toothpick.InjectConstructor

@InjectConstructor
class ScheduleDbDataSource(
    private val dao: ScheduleDao,
    private val converter: ScheduleConverter
) {

    fun getListAll(): Single<List<ScheduleDayRelative>> = dao
        .getListAll()
        .map(converter::toDomain)

    fun getList(ids: List<Int>): Single<List<ScheduleDayRelative>> = dao
        .getList(ids)
        .map(converter::toDomain)

    fun getOne(dayId: Int): Single<ScheduleDayRelative> = dao
        .getOne(dayId)
        .map(converter::toDomain)

    fun insert(items: List<ScheduleDayRelative>): Completable = Single.just(items)
        .map(converter::toDb)
        .flatMapCompletable(dao::insert)

    fun removeList(ids: List<Int>): Completable = dao.delete(ids)

    fun delete(): Completable = dao.deleteAll()
}