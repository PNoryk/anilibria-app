package tv.anilibria.feature.content.data.network.di

import okhttp3.HttpUrl
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import toothpick.InjectConstructor
import tv.anilibria.plugin.data.network.BaseUrlsProvider
import javax.inject.Provider

@InjectConstructor
class DirectRetrofitProvider(
    private val urlProvider: BaseUrlsProvider,
    private val directOkHttpProvider: DirectOkHttpProvider,
    private val converterFactory: MoshiConverterFactory
) : Provider<Retrofit> {

    override fun get(): Retrofit {
        val httpUrl = requireNotNull(HttpUrl.parse(urlProvider.api.value))
        val baseUrl = requireNotNull(httpUrl.resolve("/"))
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(directOkHttpProvider.get())
            .addConverterFactory(converterFactory)
            .build()
    }
}