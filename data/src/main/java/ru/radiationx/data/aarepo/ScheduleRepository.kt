package ru.radiationx.data.aarepo

import io.reactivex.Observable
import io.reactivex.Single
import ru.radiationx.data.acache.combiner.ScheduleCacheCombiner
import anilibria.tv.domain.entity.schedule.ScheduleDay
import ru.radiationx.data.api.datasource.ScheduleApiDataSource
import ru.radiationx.data.api.datasource.impl.ScheduleApiDataSourceImpl
import toothpick.InjectConstructor

@InjectConstructor
class ScheduleRepository(
    private val apiDataSource: ScheduleApiDataSource,
    private val cacheCombiner: ScheduleCacheCombiner
) {

    fun observeList(): Observable<List<ScheduleDay>> = cacheCombiner.observeList()

    fun getList(): Single<List<ScheduleDay>> = apiDataSource
        .getList()
        .flatMap { cacheCombiner.putList(it).toSingleDefault(it) }
        .flatMap { cacheCombiner.getList() }
}