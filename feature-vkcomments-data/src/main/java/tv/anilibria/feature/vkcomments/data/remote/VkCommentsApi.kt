package tv.anilibria.feature.vkcomments.data.remote

import okhttp3.FormBody
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url
import tv.anilibria.plugin.data.restapi.ApiResponse

interface VkCommentsApi {

    @POST
    suspend fun getBody(
        @Url url: String
    ): ResponseBody

    @POST
    suspend fun getVkComments(
        @Url url: String,
        @Body body: FormBody
    ): ApiResponse<VkCommentsResponse>

    @GET
    suspend fun checkVkBlocked(
        @Url url: String
    )
}