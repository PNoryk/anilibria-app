package tv.anilibria.module.data.network.datasource.remote

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.reactivex.Single

interface IClient {

    fun get(url: String, args: Map<String, String>): Single<String>
    fun post(url: String, args: Map<String, String>): Single<String>
    fun put(url: String, args: Map<String, String>): Single<String>
    fun delete(url: String, args: Map<String, String>): Single<String>

    fun getFull(url: String, args: Map<String, String>): Single<NetworkResponse>
    fun postFull(url: String, args: Map<String, String>): Single<NetworkResponse>
    fun putFull(url: String, args: Map<String, String>): Single<NetworkResponse>
    fun deleteFull(url: String, args: Map<String, String>): Single<NetworkResponse>

}

inline fun <reified T> getApiResponseAdapter(moshi: Moshi): JsonAdapter<ApiResponse<T>> {
    val type = Types.newParameterizedType(ApiResponse::class.java, T::class.java)
    return moshi.adapter(type)
}

inline fun <reified T> Single<String>.mapApiResponse(moshi: Moshi): Single<T> {
    return compose(ApiResponseTransformer(getApiResponseAdapter(moshi)))
}

inline fun <reified T> Single<String>.mapResponse(moshi: Moshi): Single<T> {
    return map { moshi.adapter(T::class.java).fromJson(it) }
}
