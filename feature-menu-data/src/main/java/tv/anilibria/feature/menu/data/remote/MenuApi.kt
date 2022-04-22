package tv.anilibria.feature.menu.data.remote

import okhttp3.FormBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url
import tv.anilibria.plugin.data.restapi.ApiResponse

interface MenuApi {

    @POST
    suspend fun getMenu(
        @Url url: String,
        @Body body: FormBody
    ): ApiResponse<List<LinkMenuItemResponse>>
}