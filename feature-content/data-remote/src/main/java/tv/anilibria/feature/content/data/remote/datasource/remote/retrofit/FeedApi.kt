package tv.anilibria.feature.content.data.remote.datasource.remote.retrofit

import okhttp3.FormBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url
import tv.anilibria.feature.content.data.remote.entity.app.feed.FeedResponse
import tv.anilibria.plugin.data.restapi.ApiResponse

interface FeedApi {

    @POST
    suspend fun getFeed(
        @Url url: String,
        @Body body: FormBody
    ): ApiResponse<List<FeedResponse>>
}