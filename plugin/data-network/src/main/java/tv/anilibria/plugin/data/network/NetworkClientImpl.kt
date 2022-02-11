package tv.anilibria.plugin.data.network

import io.reactivex.Single
import okhttp3.*
import javax.inject.Inject

open class NetworkClientImpl @Inject constructor(
    private val okHttpClient: DynamicOkHttpClient,
) : NetworkClient {

    companion object {
        private const val METHOD_GET = "GET"
        private const val METHOD_POST = "POST"
        private const val METHOD_PUT = "PUT"
        private const val METHOD_DELETE = "DELETE"
    }

    override fun get(url: String, args: Map<String, String>): Single<NetworkResponse> =
        request(METHOD_GET, url, args)

    override fun post(url: String, args: Map<String, String>): Single<NetworkResponse> =
        request(METHOD_POST, url, args)

    override fun put(url: String, args: Map<String, String>): Single<NetworkResponse> =
        request(METHOD_PUT, url, args)

    override fun delete(url: String, args: Map<String, String>): Single<NetworkResponse> =
        request(METHOD_DELETE, url, args)

    private fun request(
        method: String,
        url: String,
        args: Map<String, String>
    ): Single<NetworkResponse> = Single
        .fromCallable {
            val body = getRequestBody(method, args)
            val httpUrl = getHttpUrl(url, method, args)
            val request = Request.Builder()
                .url(httpUrl)
                .method(method, body)
                .build()
            okHttpClient.get().newCall(request)
        }
        .flatMap { CallExecuteSingle(it) }
        .map {
            val response = NetworkResponse(
                url = getHttpUrl(url, method, args).toString(),
                code = it.code(),
                message = it.message() ?: "Unknown http status message",
                redirect = it.request().url().toString(),
                body = it.body()?.string().orEmpty(),
            )
            if (!it.isSuccessful) {
                throw HttpException(response)
            } else {
                response
            }
        }

    private fun getRequestBody(
        method: String,
        args: Map<String, String>
    ): RequestBody? = when (method) {
        METHOD_POST -> {
            FormBody.Builder()
                .apply {
                    args.forEach {
                        add(it.key, it.value)
                    }
                }
                .build()
        }
        METHOD_PUT, METHOD_DELETE -> {
            RequestBody.create(MediaType.parse("text/plain; charset=utf-8"), "")
        }
        METHOD_GET -> null
        else -> throw Exception("Unknown method: $method")
    }

    private fun getHttpUrl(url: String, method: String, args: Map<String, String>): HttpUrl {
        var httpUrl = HttpUrl.parse(url) ?: throw Exception("URL incorrect: '$url'")
        if (method == METHOD_GET) {
            httpUrl = httpUrl.newBuilder().let { builder ->
                args.forEach { builder.addQueryParameter(it.key, it.value) }
                builder.build()
            }
        }
        return httpUrl
    }
}
