package tv.anilibria.module.data.restapi.datasource.remote.retrofit

import io.reactivex.Single
import okhttp3.FormBody
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url
import tv.anilibria.module.data.restapi.entity.app.donation.DonationDetailResponse
import tv.anilibria.module.data.restapi.entity.app.donation.DonationInfoResponse
import tv.anilibria.plugin.data.restapi.ApiResponse

interface DonationApi {

    @POST
    fun getDetails(
        @Body body: FormBody
    ): Single<ApiResponse<DonationInfoResponse>>
}