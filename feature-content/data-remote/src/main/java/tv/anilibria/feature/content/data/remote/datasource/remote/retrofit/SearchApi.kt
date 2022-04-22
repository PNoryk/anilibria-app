package tv.anilibria.feature.content.data.remote.datasource.remote.retrofit

import okhttp3.FormBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url
import tv.anilibria.feature.content.data.remote.entity.app.PageResponse
import tv.anilibria.feature.content.data.remote.entity.app.release.ReleaseResponse
import tv.anilibria.plugin.data.restapi.ApiResponse

interface SearchApi {

    @POST
    suspend fun getGenres(
        @Url url: String,
        @Body body: FormBody
    ): ApiResponse<List<String>>

    @POST
    suspend fun getYears(
        @Url url: String,
        @Body body: FormBody
    ): ApiResponse<List<String>>

    @POST
    suspend fun fastSearch(
        @Url url: String,
        @Body body: FormBody
    ): ApiResponse<List<ReleaseResponse>>

    @POST
    suspend fun search(
        @Url url: String,
        @Body body: FormBody
    ): ApiResponse<PageResponse<ReleaseResponse>>
}