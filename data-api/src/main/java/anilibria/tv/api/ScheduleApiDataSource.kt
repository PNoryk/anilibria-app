package anilibria.tv.api

import io.reactivex.Single
import anilibria.tv.domain.entity.schedule.ScheduleDay

interface ScheduleApiDataSource {
    fun getList(): Single<List<ScheduleDay>>
}