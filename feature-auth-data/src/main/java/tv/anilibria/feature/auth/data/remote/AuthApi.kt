package tv.anilibria.feature.auth.data.remote

import okhttp3.FormBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url
import tv.anilibria.plugin.data.restapi.ApiResponse

interface AuthApi {

    @POST
    suspend fun getOtpInfo(
        @Body body: FormBody
    ): ApiResponse<OtpInfoResponse>

    @POST
    suspend fun acceptOtp(
        @Body body: FormBody
    ): ApiResponse<Unit>

    @POST
    suspend fun signInOtp(
        @Body body: FormBody
    ): ApiResponse<Unit>

    @POST
    suspend fun getSocialAuthServices(
        @Body body: FormBody
    ): ApiResponse<List<SocialAuthServiceResponse>>

    @POST
    suspend fun signIn(
        @Url url: String,
        @Body body: FormBody
    ): ResponseBody

    @POST
    suspend fun signInSocial(
        @Url url: String
    ): Response<ResponseBody>

    @POST
    suspend fun signOut(
        @Url url: String
    ): ResponseBody
}