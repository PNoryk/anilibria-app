package tv.anilibria.plugin.data.network

import javax.inject.Provider

class NetworkWrapper<T>(
    private val proxyProvider: Provider<T>,
    private val directProvider: Provider<T>,
) {
    fun proxy(): T = proxyProvider.get()
    fun direct(): T = directProvider.get()
}