package tv.anilibria.plugin.data.restapi

import javax.inject.Provider


class ApiWrapper<T>(
    private val proxyProvider: Provider<T>,
    private val directProvider: Provider<T>,
) {
    fun proxy(): T = proxyProvider.get()
    fun direct(): T = directProvider.get()
}