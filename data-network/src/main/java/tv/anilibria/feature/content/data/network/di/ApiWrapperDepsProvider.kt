package tv.anilibria.feature.content.data.network.di

import toothpick.InjectConstructor
import tv.anilibria.plugin.data.network.ApiWrapperDeps
import tv.anilibria.plugin.data.network.ConfigHash
import javax.inject.Provider

@InjectConstructor
class ApiWrapperDepsProvider(
    private val configHash: ConfigHash,
    private val proxyRetrofitProvider: ProxyRetrofitProvider,
    private val directRetrofitProvider: DirectRetrofitProvider
) : Provider<ApiWrapperDeps> {

    override fun get(): ApiWrapperDeps {
        return ApiWrapperDeps(configHash, proxyRetrofitProvider, directRetrofitProvider)
    }
}