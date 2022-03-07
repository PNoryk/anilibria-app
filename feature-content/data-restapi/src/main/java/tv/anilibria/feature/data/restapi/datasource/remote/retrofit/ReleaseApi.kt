package tv.anilibria.feature.data.restapi.datasource.remote.retrofit

import okhttp3.FormBody
import retrofit2.http.Body
import retrofit2.http.POST
import tv.anilibria.feature.data.restapi.entity.app.PageResponse
import tv.anilibria.feature.data.restapi.entity.app.release.RandomReleaseResponse
import tv.anilibria.feature.data.restapi.entity.app.release.ReleaseResponse
import tv.anilibria.plugin.data.restapi.ApiResponse

interface ReleaseApi {

    @POST
    suspend fun getRandom(
        @Body body: FormBody
    ): ApiResponse<RandomReleaseResponse>

    @POST
    suspend fun getRelease(
        @Body body: FormBody
    ): ApiResponse<ReleaseResponse>

    @POST
    suspend fun getReleases(
        @Body body: FormBody
    ): ApiResponse<List<ReleaseResponse>>

    @POST
    suspend fun getPagesReleases(
        @Body body: FormBody
    ): ApiResponse<PageResponse<ReleaseResponse>>
}