package tv.anilibria.plugin.data.network

import retrofit2.Retrofit

abstract class ApiWrapper<T>(
    private val configHash: ConfigHash,
    @ProxyNetworkQualifier private val proxyProvider: NetworkAwareProvider<Retrofit>,
    @DirectNetworkQualifier private val directProvider: NetworkAwareProvider<Retrofit>,
) {

    private val awareProxyProvider = NetworkAwareProvider(configHash) {
        proxyProvider.get().create(apiClass)
    }

    private val awareDirectProvider = NetworkAwareProvider(configHash) {
        directProvider.get().create(apiClass)
    }

    protected abstract val apiClass: Class<T>

    fun proxy(): T = awareProxyProvider.get()

    fun direct(): T = awareDirectProvider.get()
}