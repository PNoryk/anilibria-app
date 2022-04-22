package tv.anilibria.feature.content.data.remote.datasource.remote.retrofit

import okhttp3.FormBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url
import tv.anilibria.feature.content.data.remote.entity.app.PageResponse
import tv.anilibria.feature.content.data.remote.entity.app.release.RandomReleaseResponse
import tv.anilibria.feature.content.data.remote.entity.app.release.ReleaseResponse
import tv.anilibria.plugin.data.restapi.ApiResponse

interface ReleaseApi {

    @POST
    suspend fun getRandom(
        @Url url: String,
        @Body body: FormBody
    ): ApiResponse<RandomReleaseResponse>

    @POST
    suspend fun getRelease(
        @Url url: String,
        @Body body: FormBody
    ): ApiResponse<ReleaseResponse>

    @POST
    suspend fun getReleases(
        @Url url: String,
        @Body body: FormBody
    ): ApiResponse<List<ReleaseResponse>>

    @POST
    suspend fun getPagesReleases(
        @Url url: String,
        @Body body: FormBody
    ): ApiResponse<PageResponse<ReleaseResponse>>
}