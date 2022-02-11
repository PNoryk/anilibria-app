package tv.anilibria.module.data.restapi.datasource.remote.retrofit

import io.reactivex.Single
import okhttp3.FormBody
import retrofit2.http.Body
import retrofit2.http.POST
import tv.anilibria.module.data.restapi.entity.app.PageResponse
import tv.anilibria.module.data.restapi.entity.app.youtube.YoutubeResponse
import tv.anilibria.plugin.data.restapi.ApiResponse

interface YoutubeApi {

    @POST
    fun getYoutube(
        @Body body: FormBody
    ): Single<ApiResponse<PageResponse<YoutubeResponse>>>
}