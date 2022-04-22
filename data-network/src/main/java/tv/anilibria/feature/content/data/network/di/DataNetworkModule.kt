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
        bind(Moshi::class.java).toProviderInstance { Moshi.Builder().build() }.providesSingleton()
        bind(MoshiConverterFactory::class.java).toProvider(ConverterFactoryProvider::class.java)
            .providesSingleton()

        bind(AppInfoInterceptor::class.java)
        bind(RemoteAddressInterceptor::class.java)
        bind(HttpLoggingInterceptor::class.java).toInstance(HttpLoggingInterceptor())
        bind(OkHttpProxyAppender::class.java)

        bind(AppCookieJar::class.java)
        bind(CookiesStorage::class.java).singleton()
        bind(LegacyCookieHolder::class.java).to(LegacyCookiesStorage::class.java)

        bind(ProxyOkHttpProvider::class.java)
        bind(DirectOkHttpProvider::class.java)

        bind(ProxyRetrofitProvider::class.java)
        bind(DirectRetrofitProvider::class.java)

        bind(NetworkUrlProvider::class.java).to(NetworkUrlProviderImpl::class.java).singleton()
        bind(ConfigHash::class.java).to(ConfigHashImpl::class.java).singleton()
        bind(ApiWrapperDeps::class.java)
            .toProvider(ApiWrapperDepsProvider::class.java)
            .providesSingleton()
    }
}