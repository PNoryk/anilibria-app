package tv.anilibria.module.data.restapi.datasource.remote.retrofit

import okhttp3.ResponseBody
import retrofit2.http.POST
import retrofit2.http.Url

interface OtherApi {

    @POST
    suspend fun getBody(
        @Url url: String
    ): ResponseBody
}