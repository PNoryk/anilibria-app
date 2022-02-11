package tv.anilibria.plugin.data.restapi

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.reactivex.Single
import tv.anilibria.plugin.data.network.NetworkResponse

inline fun <reified T> getApiResponseAdapter(moshi: Moshi): JsonAdapter<ApiResponse<T>> {
    val type = Types.newParameterizedType(ApiResponse::class.java, T::class.java)
    return moshi.adapter(type)
}

inline fun <reified T> Single<NetworkResponse>.mapApiResponse(moshi: Moshi): Single<T> {
    return compose(ApiResponseTransformer(getApiResponseAdapter(moshi)))
}

fun <T> Single<ApiResponse<T>>.handleApiResponse(): Single<T> = map {
    val handledResponse = it.handle()
    requireNotNull(handledResponse.data) {
        "Data from api response can't be null"
    }
}