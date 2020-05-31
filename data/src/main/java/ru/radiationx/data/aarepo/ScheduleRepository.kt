package ru.radiationx.data.aarepo

import io.reactivex.Observable
import io.reactivex.Single
import anilibria.tv.cache.combiner.ScheduleCacheCombiner
import anilibria.tv.domain.entity.schedule.ScheduleDay
import ru.radiationx.data.api.datasource.ScheduleApiDataSource
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