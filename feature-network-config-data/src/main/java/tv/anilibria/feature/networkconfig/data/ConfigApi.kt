package tv.anilibria.feature.networkconfig.data

import okhttp3.FormBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url
import tv.anilibria.feature.networkconfig.data.response.ApiConfigResponse
import tv.anilibria.plugin.data.restapi.ApiResponse

interface ConfigApi {

    @POST
    suspend fun checkAvailable(
        @Url url: String,
        @Body body: FormBody
    ): ApiResponse<Any>

    @POST
    suspend fun getConfig(
        @Url url: String,
        @Body body: FormBody
    ): ApiResponse<ApiConfigResponse>

    @GET
    suspend fun getReserveConfig(
        @Url url: String
    ): ApiConfigResponse
}