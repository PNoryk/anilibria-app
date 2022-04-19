package tv.anilibria.feature.appupdates.data

import toothpick.InjectConstructor
import tv.anilibria.plugin.data.network.ApiWrapper
import tv.anilibria.plugin.data.network.ApiWrapperDeps

@InjectConstructor
class UpdaterApiWrapper(
    apiWrapperDeps: ApiWrapperDeps
) : ApiWrapper<UpdaterApi>(UpdaterApi::class.java, apiWrapperDeps)