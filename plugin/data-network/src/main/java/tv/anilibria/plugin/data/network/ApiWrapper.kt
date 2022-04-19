package tv.anilibria.plugin.data.network

abstract class ApiWrapper<T>(
    private val apiClass: Class<T>,
    private val apiWrapperDeps: ApiWrapperDeps
) {

    private val awareProxyProvider = NetworkAwareProvider(apiWrapperDeps.configHash) {
        apiWrapperDeps.proxyProvider.get().create(apiClass)
    }

    private val awareDirectProvider = NetworkAwareProvider(apiWrapperDeps.configHash) {
        apiWrapperDeps.directProvider.get().create(apiClass)
    }

    fun proxy(): T = awareProxyProvider.get()

    fun direct(): T = awareDirectProvider.get()
}