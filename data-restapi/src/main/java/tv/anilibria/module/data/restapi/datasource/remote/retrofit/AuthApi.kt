package tv.anilibria.module.data.restapi.datasource.remote.retrofit

import io.reactivex.Single
import okhttp3.FormBody
import retrofit2.http.Body
import retrofit2.http.POST
import tv.anilibria.module.data.restapi.entity.app.auth.OtpInfoResponse
import tv.anilibria.module.data.restapi.entity.app.auth.SocialAuthServiceResponse
import tv.anilibria.plugin.data.restapi.ApiResponse

interface AuthApi {

    @POST
    fun getOtpInfo(
        @Body body: FormBody
    ): Single<ApiResponse<OtpInfoResponse>>

    @POST
    fun acceptOtp(
        @Body body: FormBody
    ): Single<ApiResponse<Unit>>

    @POST
    fun signInOtp(
        @Body body: FormBody
    ): Single<ApiResponse<Unit>>

    @POST
    fun getSocialAuthServices(
        @Body body: FormBody
    ): Single<ApiResponse<List<SocialAuthServiceResponse>>>
}