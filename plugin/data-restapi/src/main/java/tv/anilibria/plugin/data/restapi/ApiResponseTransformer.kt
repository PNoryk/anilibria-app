package tv.anilibria.plugin.data.restapi

import com.squareup.moshi.JsonAdapter
import io.reactivex.Single
import io.reactivex.SingleSource
import io.reactivex.SingleTransformer
import tv.anilibria.plugin.data.network.NetworkResponse

class ApiResponseTransformer<T>(
    private val adapter: JsonAdapter<ApiResponse<T>>
) : SingleTransformer<NetworkResponse, T> {

    override fun apply(upstream: Single<NetworkResponse>): SingleSource<T> = upstream.map { networkResponse ->
        val response = requireNotNull(adapter.fromJson(networkResponse.body)) {
            "ApiResponse can not be null"
        }
        response.handle().data
    }
}