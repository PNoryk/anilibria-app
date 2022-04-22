package tv.anilibria.feature.content.data.network.di

import com.squareup.moshi.Moshi
import retrofit2.converter.moshi.MoshiConverterFactory
import toothpick.InjectConstructor
import javax.inject.Provider

@InjectConstructor
class ConverterFactoryProvider(private val moshi: Moshi) : Provider<MoshiConverterFactory> {

    override fun get(): MoshiConverterFactory {
        return MoshiConverterFactory.create(moshi)
    }
}