package tv.anilibria.feature.networkconfig.data

import toothpick.InjectConstructor
import tv.anilibria.plugin.data.network.ApiWrapper
import tv.anilibria.plugin.data.network.ApiWrapperDeps

@InjectConstructor
class ConfigApiWrapper(
    apiWrapperDeps: ApiWrapperDeps
) : ApiWrapper<ConfigApi>(ConfigApi::class.java, apiWrapperDeps)