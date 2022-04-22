package tv.anilibria.feature.content.data.network.di

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import toothpick.InjectConstructor
import tv.anilibria.plugin.data.network.NetworkUrlProvider
import javax.inject.Provider

@InjectConstructor
class ProxyRetrofitProvider(
    private val networkUrlProvider: NetworkUrlProvider,
    private val proxyOkHttpProvider: ProxyOkHttpProvider,
    private val converterFactory: MoshiConverterFactory
) : Provider<Retrofit> {

    override fun get(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(networkUrlProvider.apiUrl)
            .client(proxyOkHttpProvider.get())
            .addConverterFactory(converterFactory)
            .build()
    }
}