package tv.anilibria.feature.content.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Inject
import javax.inject.Provider


class MainOkHttpProvider @Inject constructor(
    private val httpLoggingInterceptor: HttpLoggingInterceptor,
    private val remoteAddressInterceptor: RemoteAddressInterceptor
) : Provider<OkHttpClient> {

    override fun get(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .appendTimeouts()
        .addNetworkInterceptor(remoteAddressInterceptor)
        .build()
}