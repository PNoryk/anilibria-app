package ru.radiationx.data.adb.datasource

import io.reactivex.Completable
import io.reactivex.Single
import ru.radiationx.data.adb.dao.ScheduleDao
import ru.radiationx.data.adb.datasource.converters.ScheduleConverter
import ru.radiationx.data.adomain.relative.ScheduleDayRelative
import ru.radiationx.data.adomain.release.Release
import toothpick.InjectConstructor

@InjectConstructor
class ScheduleDbDataSource(
    private val scheduleDao: ScheduleDao,
    private val scheduleConverter: ScheduleConverter
) {

    fun getListAll(): Single<List<ScheduleDayRelative>> = scheduleDao
        .getListAll()
        .map(scheduleConverter::toDomain)

    fun getOne(releaseId: Int): Single<ScheduleDayRelative> = scheduleDao
        .getOne(releaseId)
        .map(scheduleConverter::toDomain)

    fun insert(items: List<ScheduleDayRelative>): Completable = Single.just(items)
        .map(scheduleConverter::toDb)
        .flatMapCompletable(scheduleDao::insert)

    fun delete(): Completable = scheduleDao.deleteAll()
}