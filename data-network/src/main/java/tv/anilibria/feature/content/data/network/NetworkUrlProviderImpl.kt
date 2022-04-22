package tv.anilibria.feature.content.data.network

import kotlinx.coroutines.runBlocking
import toothpick.InjectConstructor
import tv.anilibria.feature.networkconfig.data.address.ApiConfigController
import tv.anilibria.plugin.data.network.NetworkUrlProvider

@InjectConstructor
class NetworkUrlProviderImpl(
    private val apiConfigController: ApiConfigController
) : NetworkUrlProvider {

    override val baseUrl: String
        get() = runBlocking {
            apiConfigController.getActive().base
        }

    override val apiUrl: String
        get() = runBlocking {
            apiConfigController.getActive().api
        }
}