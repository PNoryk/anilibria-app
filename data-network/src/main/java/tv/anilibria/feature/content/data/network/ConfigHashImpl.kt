package tv.anilibria.feature.content.data.network

import kotlinx.coroutines.runBlocking
import toothpick.InjectConstructor
import tv.anilibria.feature.networkconfig.data.address.ApiConfigController
import tv.anilibria.plugin.data.network.ConfigHash

@InjectConstructor
class ConfigHashImpl(
    private val configController: ApiConfigController
) : ConfigHash {

    override fun getHash(): Int {
        return runBlocking {
            configController.getActive().hashCode()
        }
    }
}