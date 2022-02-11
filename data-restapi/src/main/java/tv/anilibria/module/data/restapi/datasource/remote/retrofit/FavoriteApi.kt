package tv.anilibria.module.data.restapi.datasource.remote.retrofit

import io.reactivex.Single
import okhttp3.FormBody
import retrofit2.http.Body
import retrofit2.http.POST
import tv.anilibria.module.data.restapi.entity.app.PageResponse
import tv.anilibria.module.data.restapi.entity.app.release.ReleaseResponse
import tv.anilibria.plugin.data.restapi.ApiResponse

interface FavoriteApi {

    @POST
    fun getFavorites(
        @Body body: FormBody
    ): Single<ApiResponse<PageResponse<ReleaseResponse>>>

    @POST
    fun addFavorite(
        @Body body: FormBody
    ): Single<ApiResponse<ReleaseResponse>>

    @POST
    fun deleteFavorite(
        @Body body: FormBody
    ): Single<ApiResponse<ReleaseResponse>>
}