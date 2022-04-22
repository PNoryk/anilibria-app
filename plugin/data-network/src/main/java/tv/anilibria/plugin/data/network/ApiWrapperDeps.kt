package tv.anilibria.plugin.data.network

import retrofit2.Retrofit
import javax.inject.Provider

class ApiWrapperDeps(
    val configHash: ConfigHash,
    private val proxyProvider: Provider<Retrofit>,
    private val directProvider: Provider<Retrofit>,
) {
    val awareProxyProvider = NetworkAwareProvider(configHash, proxyProvider)
    val awareDirectProvider = NetworkAwareProvider(configHash, directProvider)
}