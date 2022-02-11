package tv.anilibria.module.data.restapi.datasource.remote.retrofit

import io.reactivex.Single
import okhttp3.FormBody
import retrofit2.http.Body
import retrofit2.http.POST
import tv.anilibria.module.data.restapi.entity.app.other.LinkMenuItemResponse
import tv.anilibria.module.data.restapi.entity.app.page.VkCommentsResponse
import tv.anilibria.plugin.data.restapi.ApiResponse

interface OtherApi {

    @POST
    fun getMenu(
        @Body body: FormBody
    ): Single<ApiResponse<List<LinkMenuItemResponse>>>


    @POST
    fun getVkComments(
        @Body body: FormBody
    ): Single<ApiResponse<VkCommentsResponse>>
}