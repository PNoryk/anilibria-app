package tv.anilibria.module.data.network

import okhttp3.logging.HttpLoggingInterceptor
import tv.anilibria.plugin.shared.appinfo.SharedBuildConfig

object Temp {
    fun httpLoggingInterceptor(sharedBuildConfig: SharedBuildConfig) =
        HttpLoggingInterceptor().apply {
            val level = if (sharedBuildConfig.debug) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
            setLevel(level)
        }
}

