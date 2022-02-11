package tv.anilibria.module.data.restapi.datasource.remote.retrofit

import io.reactivex.Single
import okhttp3.FormBody
import retrofit2.http.Body
import retrofit2.http.POST
import tv.anilibria.module.data.restapi.entity.app.PageResponse
import tv.anilibria.module.data.restapi.entity.app.release.RandomReleaseResponse
import tv.anilibria.module.data.restapi.entity.app.release.ReleaseResponse
import tv.anilibria.plugin.data.restapi.ApiResponse

interface ReleaseApi {

    @POST
    fun getRandom(
        @Body body: FormBody
    ): Single<ApiResponse<RandomReleaseResponse>>

    @POST
    fun getRelease(
        @Body body: FormBody
    ): Single<ApiResponse<ReleaseResponse>>

    @POST
    fun getReleases(
        @Body body: FormBody
    ): Single<ApiResponse<List<ReleaseResponse>>>

    @POST
    fun getPagesReleases(
        @Body body: FormBody
    ): Single<ApiResponse<PageResponse<ReleaseResponse>>>
}