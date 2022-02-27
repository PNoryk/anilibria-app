package tv.anilibria.feature.page.data.remote

import okhttp3.ResponseBody
import retrofit2.http.POST
import retrofit2.http.Url

interface PageApi {

    @POST
    suspend fun getBody(
        @Url url: String
    ): ResponseBody
}