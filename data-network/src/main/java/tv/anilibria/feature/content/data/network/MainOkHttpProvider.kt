package tv.anilibria.feature.content.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import toothpick.InjectConstructor
import javax.inject.Provider

@InjectConstructor
class MainOkHttpProvider(
    private val httpLoggingInterceptor: HttpLoggingInterceptor,
    private val remoteAddressInterceptor: RemoteAddressInterceptor
) : Provider<OkHttpClient> {

    override fun get(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .appendTimeouts()
        .addNetworkInterceptor(remoteAddressInterceptor)
        .build()
}