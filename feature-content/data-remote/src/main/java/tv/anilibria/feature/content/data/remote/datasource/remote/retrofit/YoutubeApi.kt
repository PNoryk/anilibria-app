package tv.anilibria.feature.content.data.remote.datasource.remote.retrofit

import okhttp3.FormBody
import retrofit2.http.Body
import retrofit2.http.POST
import tv.anilibria.feature.content.data.remote.entity.app.PageResponse
import tv.anilibria.feature.content.data.remote.entity.app.youtube.YoutubeResponse
import tv.anilibria.plugin.data.restapi.ApiResponse

interface YoutubeApi {

    @POST
    suspend fun getYoutube(
        @Body body: FormBody
    ): ApiResponse<PageResponse<YoutubeResponse>>
}