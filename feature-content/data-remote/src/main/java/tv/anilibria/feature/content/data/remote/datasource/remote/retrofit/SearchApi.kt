package tv.anilibria.feature.content.data.remote.datasource.remote.retrofit

import okhttp3.FormBody
import retrofit2.http.Body
import retrofit2.http.POST
import tv.anilibria.feature.content.data.remote.entity.app.PageResponse
import tv.anilibria.feature.content.data.remote.entity.app.release.ReleaseResponse
import tv.anilibria.plugin.data.restapi.ApiResponse

interface SearchApi {

    @POST
    suspend fun getGenres(
        @Body body: FormBody
    ): ApiResponse<List<String>>

    @POST
    suspend fun getYears(
        @Body body: FormBody
    ): ApiResponse<List<String>>

    @POST
    suspend fun fastSearch(
        @Body body: FormBody
    ): ApiResponse<List<ReleaseResponse>>

    @POST
    suspend fun search(
        @Body body: FormBody
    ): ApiResponse<PageResponse<ReleaseResponse>>
}