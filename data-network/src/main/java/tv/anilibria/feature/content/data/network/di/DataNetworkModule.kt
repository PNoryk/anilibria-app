package tv.anilibria.feature.content.data.network.di

import com.squareup.moshi.Moshi
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.moshi.MoshiConverterFactory
import toothpick.config.Module
import tv.anilibria.feature.content.data.network.*
import tv.anilibria.feature.networkconfig.data.okhttp.OkHttpProxyAppender
import tv.anilibria.plugin.data.network.ApiWrapperDeps
import tv.anilibria.plugin.data.network.ConfigHash
import tv.anilibria.plugin.data.network.NetworkUrlProvider

class DataNetworkModule : Module() {

    init {
        bind(Moshi::class.java)
            .toProviderInstance { Moshi.Builder().build() }
            .providesSingleton()
        bind(MoshiConverterFactory::class.java)
            .toProvider(ConverterFactoryProvider::class.java)
            .providesSingleton()

        bind(AppInfoInterceptor::class.java).singleton()
        bind(RemoteAddressInterceptor::class.java).singleton()
        bind(HttpLoggingInterceptor::class.java)
            .toProvider(LoggingInterceptorProvider::class.java)
            .providesSingleton()
        bind(OkHttpProxyAppender::class.java).singleton()

        bind(AppCookieJar::class.java).singleton()
        bind(CookiesStorage::class.java).singleton()
        bind(LegacyCookieHolder::class.java)
            .to(LegacyCookiesStorage::class.java)
            .singleton()

        bind(ProxyOkHttpProvider::class.java).singleton()
        bind(DirectOkHttpProvider::class.java).singleton()

        bind(ProxyRetrofitProvider::class.java).singleton()
        bind(DirectRetrofitProvider::class.java).singleton()

        bind(NetworkUrlProvider::class.java)
            .to(NetworkUrlProviderImpl::class.java)
            .singleton()
        bind(ConfigHash::class.java).to(ConfigHashImpl::class.java).singleton()
        bind(ApiWrapperDeps::class.java)
            .toProvider(ApiWrapperDepsProvider::class.java)
            .providesSingleton()
    }
}