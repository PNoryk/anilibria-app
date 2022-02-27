package tv.anilibria.feature.user.data.remote

import okhttp3.FormBody
import retrofit2.http.Body
import retrofit2.http.POST
import tv.anilibria.plugin.data.restapi.ApiResponse

interface UserApi {

    @POST
    suspend fun getUser(
        @Body body: FormBody
    ): ApiResponse<UserResponse>
}