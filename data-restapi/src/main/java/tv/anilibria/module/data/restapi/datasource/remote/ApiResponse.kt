package tv.anilibria.module.data.restapi.datasource.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiResponse<T>(
    @Json(name = "status") val status: Boolean?,
    @Json(name = "data") val data: T?,
    @Json(name = "error") val error: ApiErrorResponse?
) {

    fun handleError(): ApiResponse<T> = when {
        status == true && data != null -> this
        error != null -> throw ApiException(error.code, error.message, error.description)
        else -> throw IllegalArgumentException("Wrong response")
    }
}
