package tv.anilibria.feature.content.data.network.di

import android.util.Log
import okhttp3.logging.HttpLoggingInterceptor
import toothpick.InjectConstructor
import tv.anilibria.plugin.shared.appinfo.SharedBuildConfig
import javax.inject.Provider

@InjectConstructor
class LoggingInterceptorProvider(
    private val sharedBuildConfig: SharedBuildConfig
) : Provider<HttpLoggingInterceptor> {

    override fun get(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor({
            Log.d("kekeke","httplog $it")
        }).apply {
            val level = if (sharedBuildConfig.debug) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
            setLevel(level)
        }
    }
}