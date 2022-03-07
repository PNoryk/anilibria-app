package tv.anilibria.module.data.restapi.datasource.remote.retrofit

import okhttp3.FormBody
import retrofit2.http.Body
import retrofit2.http.POST
import tv.anilibria.module.data.restapi.entity.app.PageResponse
import tv.anilibria.module.data.restapi.entity.app.release.ReleaseResponse
import tv.anilibria.plugin.data.restapi.ApiResponse

interface FavoriteApi {

    @POST
    suspend fun getFavorites(
        @Body body: FormBody
    ): ApiResponse<PageResponse<ReleaseResponse>>

    @POST
    suspend fun addFavorite(
        @Body body: FormBody
    ): ApiResponse<ReleaseResponse>

    @POST
    suspend fun deleteFavorite(
        @Body body: FormBody
    ): ApiResponse<ReleaseResponse>
}