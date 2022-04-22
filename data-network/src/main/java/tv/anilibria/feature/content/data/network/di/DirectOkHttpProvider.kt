package tv.anilibria.feature.content.data.network.di

import okhttp3.OkHttpClient
import toothpick.InjectConstructor
import tv.anilibria.feature.content.data.network.RemoteAddressInterceptor
import tv.anilibria.feature.content.data.network.appendLogger
import tv.anilibria.feature.content.data.network.appendTimeouts
import tv.anilibria.plugin.shared.appinfo.SharedBuildConfig
import javax.inject.Provider

@InjectConstructor
class DirectOkHttpProvider(
    private val sharedBuildConfig: SharedBuildConfig,
    private val remoteAddressInterceptor: RemoteAddressInterceptor
) : Provider<OkHttpClient> {

    override fun get(): OkHttpClient = OkHttpClient.Builder()
        .appendTimeouts()
        .appendLogger("OkHttpDirect", sharedBuildConfig)
        .addNetworkInterceptor(remoteAddressInterceptor)
        .build()
}