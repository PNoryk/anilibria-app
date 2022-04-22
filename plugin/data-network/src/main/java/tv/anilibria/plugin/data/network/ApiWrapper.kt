package tv.anilibria.plugin.data.network

abstract class ApiWrapper<T>(
    private val apiClass: Class<T>,
    private val apiWrapperDeps: ApiWrapperDeps
) {

    private val awareProxyProvider = NetworkAwareProvider(apiWrapperDeps.configHash) {
        apiWrapperDeps.awareProxyProvider.get().create(apiClass)
    }

    private val awareDirectProvider = NetworkAwareProvider(apiWrapperDeps.configHash) {
        apiWrapperDeps.awareDirectProvider.get().create(apiClass)
    }

    fun proxy(): T {
        return awareProxyProvider.get()
    }

    fun direct(): T {
        return awareDirectProvider.get()
    }
}