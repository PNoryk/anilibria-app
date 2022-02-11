package tv.anilibria.module.data.restapi.datasource.remote.retrofit

import io.reactivex.Single
import okhttp3.FormBody
import retrofit2.http.Body
import retrofit2.http.POST
import tv.anilibria.module.data.restapi.entity.app.feed.FeedResponse
import tv.anilibria.plugin.data.restapi.ApiResponse

interface FeedApi {

    @POST
    fun getFeed(
        @Body body: FormBody
    ): Single<ApiResponse<List<FeedResponse>>>
}