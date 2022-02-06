package tv.anilibria.module.data.network.datasource.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonClass
import io.reactivex.Single
import io.reactivex.SingleTransformer

@JsonClass(generateAdapter = true)
data class ApiResponse<T>(
    @Json(name = "status") val status: Boolean?,
    @Json(name = "data") val data: T?,
    @Json(name = "error") val error: ApiErrorResponse?
) {

    fun handleError(): Single<ApiResponse<T>> = when {
        status == true && data != null -> Single.just(this)
        error != null -> Single.error(ApiError(error.code, error.message, error.description))
        else -> Single.error(Exception("Wrong response"))
    }

    companion object {
        fun <T> fetchResult(
            adapter: JsonAdapter<ApiResponse<T>>
        ): SingleTransformer<String, T> = SingleTransformer { inputData ->
            inputData.flatMap { jsonString ->
                val response = requireNotNull(adapter.fromJson(jsonString)) {
                    "ApiResponse can not be null"
                }
                response.handleError()
            }.map { t -> t.data }
        }
    }
}
