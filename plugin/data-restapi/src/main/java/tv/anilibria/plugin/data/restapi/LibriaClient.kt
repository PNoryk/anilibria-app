package tv.anilibria.plugin.data.restapi

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.reactivex.Single
import tv.anilibria.plugin.data.network.NetworkClient
import tv.anilibria.plugin.data.network.NetworkResponse
import java.lang.reflect.Type

class ApiLibriaClient(
    client: NetworkClient,
    apiConfigProvider: ApiConfigProvider,
    moshi: Moshi
) : LibriaClient(client, apiConfigProvider, moshi)

class DefaultLibriaClient(
    client: NetworkClient,
    apiConfigProvider: ApiConfigProvider,
    moshi: Moshi
) : LibriaClient(client, apiConfigProvider, moshi)

open class LibriaClient(
    private val client: NetworkClient,
    private val apiConfigProvider: ApiConfigProvider,
    private val moshi: Moshi
) {

    fun request(url: String? = null, args: Map<String, String>? = null): NetworkLibriaRequest {
        return NetworkLibriaRequest(
            client,
            moshi,
            url ?: apiConfigProvider.apiUrl,
            args ?: emptyMap()
        )
    }
}

abstract class BaseLibriaRequest<T>(
    val url: String,
    val args: Map<String, String>
) {
    abstract fun get(): Single<T>
    abstract fun post(): Single<T>
    abstract fun put(): Single<T>
    abstract fun delete(): Single<T>
}

class NetworkLibriaRequest(
    private val networkClient: NetworkClient,
    private val moshi: Moshi,
    url: String,
    args: Map<String, String>
) : BaseLibriaRequest<NetworkResponse>(url, args) {

    fun <T> typedResponse(type: Type): TypedLibriaRequest<T> {
        val adapter = moshi.adapter<T>(type)
        return TypedLibriaRequest(networkClient, adapter, url, args)
    }

    fun <T> typedResponse(adapter: JsonAdapter<T>): TypedLibriaRequest<T> {
        return TypedLibriaRequest(networkClient, adapter, url, args)
    }

    fun <T> typedApiResponse(type: Type): TypedLibriaApiRequest<T> {
        val apiType = Types.newParameterizedType(ApiResponse::class.java, type)
        val adapter = moshi.adapter<ApiResponse<T>>(apiType)
        return TypedLibriaApiRequest(networkClient, adapter, url, args)
    }

    fun <T> typedApiResponse(adapter: JsonAdapter<ApiResponse<T>>): TypedLibriaApiRequest<T> {
        return TypedLibriaApiRequest(networkClient, adapter, url, args)
    }

    override fun get(): Single<NetworkResponse> = networkClient.get(url, args)

    override fun post(): Single<NetworkResponse> = networkClient.post(url, args)

    override fun put(): Single<NetworkResponse> = networkClient.put(url, args)

    override fun delete(): Single<NetworkResponse> = networkClient.delete(url, args)
}


class TypedLibriaRequest<T>(
    private val networkClient: NetworkClient,
    private val adapter: JsonAdapter<T>,
    url: String,
    args: Map<String, String>
) : BaseLibriaRequest<T>(url, args) {

    override fun get(): Single<T> = networkClient.get(url, args)
        .map { adapter.fromJson(it.body) }

    override fun post(): Single<T> = networkClient.post(url, args)
        .map { adapter.fromJson(it.body) }

    override fun put(): Single<T> = networkClient.put(url, args)
        .map { adapter.fromJson(it.body) }

    override fun delete(): Single<T> = networkClient.delete(url, args)
        .map { adapter.fromJson(it.body) }

}

class TypedLibriaApiRequest<T>(
    private val networkClient: NetworkClient,
    private val adapter: JsonAdapter<ApiResponse<T>>,
    url: String,
    args: Map<String, String>
) : BaseLibriaRequest<T>(url, args) {

    override fun get(): Single<T> = networkClient.get(url, args)
        .compose(ApiResponseTransformer(adapter))

    override fun post(): Single<T> = networkClient.post(url, args)
        .compose(ApiResponseTransformer(adapter))

    override fun put(): Single<T> = networkClient.put(url, args)
        .compose(ApiResponseTransformer(adapter))

    override fun delete(): Single<T> = networkClient.delete(url, args)
        .compose(ApiResponseTransformer(adapter))
}


class Ex(
    val heheh: LibriaClient,
    val moshi: Moshi
) {

    fun getSomeShiet(): Single<List<String>> {
        return heheh.request("", emptyMap())
            .typedResponse<List<String>>(List::class.java)
            .get()
    }

    fun getSomeShiet1(): Single<NetworkResponse> {

        return heheh.request().get()
    }


    fun getSomeShiet2(): Single<List<String>> {
        val type = Types.newParameterizedType(List::class.java, String::class.java)
        return heheh.request()
            .typedApiResponse<List<String>>(type)
            .get()
    }
}