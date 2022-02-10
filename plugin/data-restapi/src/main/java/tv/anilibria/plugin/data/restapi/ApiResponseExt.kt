package tv.anilibria.plugin.data.restapi

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.reactivex.Single

inline fun <reified T> getApiResponseAdapter(moshi: Moshi): JsonAdapter<ApiResponse<T>> {
    val type = Types.newParameterizedType(ApiResponse::class.java, T::class.java)
    return moshi.adapter(type)
}

inline fun <reified T> Single<String>.mapApiResponse(moshi: Moshi): Single<T> {
    return compose(ApiResponseTransformer(getApiResponseAdapter(moshi)))
}
