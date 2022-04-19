package tv.anilibria.feature.content.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import toothpick.InjectConstructor
import tv.anilibria.feature.networkconfig.data.okhttp.OkHttpProxyAppender
import javax.inject.Provider

@InjectConstructor
class ApiOkHttpProvider(
    private val appCookieJar: AppCookieJar,
    private val proxyAppender: OkHttpProxyAppender,
    private val httpLoggingInterceptor: HttpLoggingInterceptor,
    private val remoteAddressInterceptor: RemoteAddressInterceptor,
    private val appInfoInterceptor: AppInfoInterceptor
) : Provider<OkHttpClient> {

    override fun get(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .appendTimeouts()
        .apply { proxyAppender.appendTo(this) }
        .addNetworkInterceptor(remoteAddressInterceptor)
        .addInterceptor(appInfoInterceptor)
        .cookieJar(appCookieJar)
        .build()
}