package tv.anilibria.module.data.restapi.datasource.remote.retrofit

import okhttp3.FormBody
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url
import tv.anilibria.module.data.restapi.entity.app.other.LinkMenuItemResponse
import tv.anilibria.module.data.restapi.entity.app.page.VkCommentsResponse
import tv.anilibria.plugin.data.restapi.ApiResponse

interface OtherApi {

    @POST
    suspend fun getMenu(
        @Body body: FormBody
    ): ApiResponse<List<LinkMenuItemResponse>>

    @POST
    suspend fun getBody(
        @Url url: String
    ): ResponseBody

    @POST
    suspend fun getVkComments(
        @Body body: FormBody
    ): ApiResponse<VkCommentsResponse>

    @GET
    suspend fun checkVkBlocked(
        @Url url: String
    )
}