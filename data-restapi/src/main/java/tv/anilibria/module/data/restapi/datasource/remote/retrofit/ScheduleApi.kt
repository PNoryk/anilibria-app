package tv.anilibria.module.data.restapi.datasource.remote.retrofit

import io.reactivex.Single
import okhttp3.FormBody
import retrofit2.http.Body
import retrofit2.http.POST
import tv.anilibria.module.data.restapi.entity.app.schedule.ScheduleDayResponse
import tv.anilibria.plugin.data.restapi.ApiResponse

interface ScheduleApi {

    @POST
    fun getSchedule(
        @Body body: FormBody
    ): Single<ApiResponse<List<ScheduleDayResponse>>>
}