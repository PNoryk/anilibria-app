package tv.anilibria.feature.content.data.network.di

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import toothpick.InjectConstructor
import tv.anilibria.feature.content.data.network.RemoteAddressInterceptor
import tv.anilibria.feature.content.data.network.appendTimeouts
import javax.inject.Provider

@InjectConstructor
class DirectOkHttpProvider(
    private val httpLoggingInterceptor: HttpLoggingInterceptor,
    private val remoteAddressInterceptor: RemoteAddressInterceptor
) : Provider<OkHttpClient> {

    override fun get(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .appendTimeouts()
        .addNetworkInterceptor(remoteAddressInterceptor)
        .build()
}