package tv.anilibria.module.domain.remote

import io.reactivex.Single
import tv.anilibria.module.domain.entity.schedule.ScheduleDay

interface ScheduleRemoteDataSource {
    fun getSchedule(): Single<List<ScheduleDay>>
}