package tv.anilibria.feature.content.data.network.di

import okhttp3.OkHttpClient
import toothpick.InjectConstructor
import tv.anilibria.feature.content.data.network.*
import tv.anilibria.feature.networkconfig.data.okhttp.OkHttpProxyAppender
import tv.anilibria.plugin.shared.appinfo.SharedBuildConfig
import javax.inject.Provider

@InjectConstructor
class ProxyOkHttpProvider(
    private val appCookieJar: AppCookieJar,
    private val sharedBuildConfig: SharedBuildConfig,
    private val proxyAppender: OkHttpProxyAppender,
    private val remoteAddressInterceptor: RemoteAddressInterceptor,
    private val appInfoInterceptor: AppInfoInterceptor
) : Provider<OkHttpClient> {

    override fun get(): OkHttpClient = OkHttpClient.Builder()
        .appendTimeouts()
        .appendLogger("OkHttpProxy", sharedBuildConfig)
        .apply { proxyAppender.appendTo(this) }
        .addNetworkInterceptor(remoteAddressInterceptor)
        .addInterceptor(appInfoInterceptor)
        .cookieJar(appCookieJar)
        .build()
}