package tv.anilibria.feature.user.data.remote

import okhttp3.FormBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url
import tv.anilibria.plugin.data.restapi.ApiResponse

interface UserApi {

    @POST
    suspend fun getUser(
        @Url url: String,
        @Body body: FormBody
    ): ApiResponse<UserResponse>
}