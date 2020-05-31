package ru.radiationx.data.api.datasource

import io.reactivex.Single
import anilibria.tv.domain.entity.schedule.ScheduleDay

interface ScheduleApiDataSource {
    fun getList(): Single<List<ScheduleDay>>
}