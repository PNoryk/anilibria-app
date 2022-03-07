package tv.anilibria.feature.content.data.network

import okhttp3.Interceptor
import okhttp3.Response

class RemoteAddressInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val hostAddress = chain.connection()?.route()?.socketAddress()?.address?.hostAddress
        val newRequest = chain.request().newBuilder().apply {
            if (hostAddress != null) {
                header("Remote-Address", hostAddress)
            }
        }.build()
        return chain.proceed(newRequest)
    }
}