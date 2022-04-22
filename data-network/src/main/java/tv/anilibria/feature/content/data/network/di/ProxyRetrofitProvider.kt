package tv.anilibria.feature.content.data.network.di

import android.util.Log
import okhttp3.HttpUrl
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
        val httpUrl = requireNotNull(HttpUrl.parse(networkUrlProvider.apiUrl))
        val baseUrl = requireNotNull(httpUrl.resolve("/"))
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(proxyOkHttpProvider.get())
            .addConverterFactory(converterFactory)
            .build()
    }
}