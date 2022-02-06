package tv.anilibria.module.data.network.datasource.remote.api

import io.reactivex.Single
import tv.anilibria.module.domain.entity.schedule.ScheduleDay

interface ScheduleRemoteDataSource {
    fun getSchedule(): Single<List<ScheduleDay>>
}