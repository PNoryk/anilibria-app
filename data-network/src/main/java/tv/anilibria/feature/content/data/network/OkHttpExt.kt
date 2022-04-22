package tv.anilibria.feature.content.data.network

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import tv.anilibria.plugin.shared.appinfo.SharedBuildConfig
import java.util.concurrent.TimeUnit

fun OkHttpClient.Builder.appendTimeouts(): OkHttpClient.Builder {
    callTimeout(25, TimeUnit.SECONDS)
    connectTimeout(15, TimeUnit.SECONDS)
    readTimeout(15, TimeUnit.SECONDS)
    writeTimeout(15, TimeUnit.SECONDS)
    return this
}

fun OkHttpClient.Builder.appendLogger(
    tag: String,
    sharedBuildConfig: SharedBuildConfig
): OkHttpClient.Builder {
    val interceptor = HttpLoggingInterceptor {
        Log.d(tag, it)
    }
    interceptor.level = if (sharedBuildConfig.debug) {
        HttpLoggingInterceptor.Level.BODY
    } else {
        HttpLoggingInterceptor.Level.NONE
    }
    addInterceptor(interceptor)
    return this
}
