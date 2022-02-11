package tv.anilibria.feature.networkconfig.data

import io.reactivex.Single
import okhttp3.FormBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url
import tv.anilibria.feature.networkconfig.data.response.ApiConfigResponse
import tv.anilibria.plugin.data.restapi.ApiResponse

interface ConfigApi {

    @POST
    fun checkAvailable(
        @Url url: String,
        @Body body: FormBody
    ): Single<ApiResponse<ApiConfigResponse>>

    @POST
    fun getConfig(
        @Body body: FormBody
    ): Single<ApiResponse<ApiConfigResponse>>

    @GET
    fun getReserveConfig(
        @Url url: String
    ): Single<ApiConfigResponse>
}