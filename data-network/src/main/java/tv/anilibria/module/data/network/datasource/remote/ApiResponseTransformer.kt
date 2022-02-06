package tv.anilibria.module.data.network.datasource.remote

import com.squareup.moshi.JsonAdapter
import io.reactivex.Single
import io.reactivex.SingleSource
import io.reactivex.SingleTransformer

class ApiResponseTransformer<T>(
    private val adapter: JsonAdapter<ApiResponse<T>>
) : SingleTransformer<String, T> {

    override fun apply(upstream: Single<String>): SingleSource<T> = upstream.map { jsonString ->
        val response = requireNotNull(adapter.fromJson(jsonString)) {
            "ApiResponse can not be null"
        }
        response.handleError().data
    }
}