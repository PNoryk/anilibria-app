package ru.radiationx.data.api.datasource

import io.reactivex.Single
import ru.radiationx.data.adomain.entity.schedule.ScheduleDay

interface ScheduleApiDataSource {
    fun getList(): Single<List<ScheduleDay>>
}