package tv.anilibria.feature.appupdates.data

import okhttp3.FormBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url
import tv.anilibria.feature.appupdates.data.response.UpdateDataResponse
import tv.anilibria.plugin.data.restapi.ApiResponse

interface UpdaterApi {

    @POST
    suspend fun checkUpdate(
        @Body body: FormBody
    ): ApiResponse<UpdateDataResponse>

    @GET
    suspend fun checkReserve(
        @Url url: String
    ): UpdateDataResponse
}