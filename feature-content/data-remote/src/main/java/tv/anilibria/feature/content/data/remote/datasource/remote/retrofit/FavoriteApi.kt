package tv.anilibria.feature.content.data.remote.datasource.remote.retrofit

import okhttp3.FormBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url
import tv.anilibria.feature.content.data.remote.entity.app.PageResponse
import tv.anilibria.feature.content.data.remote.entity.app.release.ReleaseResponse
import tv.anilibria.plugin.data.restapi.ApiResponse

interface FavoriteApi {

    @POST
    suspend fun getFavorites(
        @Url url: String,
        @Body body: FormBody
    ): ApiResponse<PageResponse<ReleaseResponse>>

    @POST
    suspend fun addFavorite(
        @Url url: String,
        @Body body: FormBody
    ): ApiResponse<ReleaseResponse>

    @POST
    suspend fun deleteFavorite(
        @Url url: String,
        @Body body: FormBody
    ): ApiResponse<ReleaseResponse>
}