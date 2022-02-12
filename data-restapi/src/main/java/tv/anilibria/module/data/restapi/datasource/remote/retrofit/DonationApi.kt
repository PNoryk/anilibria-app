package tv.anilibria.module.data.restapi.datasource.remote.retrofit

import okhttp3.FormBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url
import tv.anilibria.module.data.restapi.entity.app.donation.DonationInfoResponse
import tv.anilibria.plugin.data.restapi.ApiResponse

interface DonationApi {

    @POST
    suspend fun getDetails(
        @Body body: FormBody
    ): ApiResponse<DonationInfoResponse>

    @POST
    suspend fun createYooMoneyPayLink(
        @Url url: String,
        @Body body: FormBody
    ): Response<ResponseBody>
}