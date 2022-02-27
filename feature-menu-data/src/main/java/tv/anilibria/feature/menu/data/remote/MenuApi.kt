package tv.anilibria.feature.menu.data.remote

import okhttp3.FormBody
import retrofit2.http.Body
import retrofit2.http.POST
import tv.anilibria.plugin.data.restapi.ApiResponse

interface MenuApi {

    @POST
    suspend fun getMenu(
        @Body body: FormBody
    ): ApiResponse<List<LinkMenuItemResponse>>
}