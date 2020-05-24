package ru.radiationx.data.api.service.schedule

import io.reactivex.Single
import retrofit2.http.FieldMap
import ru.radiationx.data.api.common.ApiBaseResponse
import ru.radiationx.data.entity.app.schedule.ScheduleDay

interface ScheduleApi {

    fun getList(@FieldMap fields: Map<String, String>): Single<ApiBaseResponse<List<ScheduleDay>>>
}