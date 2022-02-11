package tv.anilibria.module.data.restapi.datasource.remote.retrofit

import io.reactivex.Single
import okhttp3.FormBody
import retrofit2.http.Body
import retrofit2.http.POST
import tv.anilibria.module.data.restapi.entity.app.PageResponse
import tv.anilibria.module.data.restapi.entity.app.release.ReleaseResponse
import tv.anilibria.plugin.data.restapi.ApiResponse

interface SearchApi {

    @POST
    fun getGenres(
        @Body body: FormBody
    ): Single<ApiResponse<List<String>>>

    @POST
    fun getYears(
        @Body body: FormBody
    ): Single<ApiResponse<List<String>>>

    @POST
    fun fastSearch(
        @Body body: FormBody
    ): Single<ApiResponse<List<ReleaseResponse>>>

    @POST
    fun search(
        @Body body: FormBody
    ): Single<ApiResponse<PageResponse<ReleaseResponse>>>
}