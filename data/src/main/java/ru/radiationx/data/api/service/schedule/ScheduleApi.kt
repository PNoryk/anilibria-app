package ru.radiationx.data.api.service.schedule

import io.reactivex.Single
import ru.radiationx.data.api.remote.common.ApiBaseResponse
import ru.radiationx.data.entity.app.schedule.ScheduleDay

interface ScheduleApi {

    fun getList(): Single<ApiBaseResponse<List<ScheduleDay>>>
}