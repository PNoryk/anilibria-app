package tv.anilibria.feature.menu.data.remote

import toothpick.InjectConstructor
import tv.anilibria.plugin.data.network.ApiWrapper
import tv.anilibria.plugin.data.network.ApiWrapperDeps

@InjectConstructor
class MenuApiWrapper(
    apiWrapperDeps: ApiWrapperDeps
) : ApiWrapper<MenuApi>(MenuApi::class.java, apiWrapperDeps)