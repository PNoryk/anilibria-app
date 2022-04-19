package tv.anilibria.feature.content.data.network

import okhttp3.logging.HttpLoggingInterceptor
import toothpick.ktp.binding.bind
import toothpick.ktp.binding.module
import tv.anilibria.feature.networkconfig.data.okhttp.OkHttpProxyAppender


val dataNetworkModule = module {
    bind<AppInfoInterceptor>()
    bind<RemoteAddressInterceptor>()
    bind<HttpLoggingInterceptor>().toInstance(HttpLoggingInterceptor())
    bind<OkHttpProxyAppender>()

    bind<AppCookieJar>()
    bind<LegacyCookieHolder>().toClass<LegacyCookiesStorage>()

    bind<ApiOkHttpProvider>()
    bind<MainOkHttpProvider>()
}