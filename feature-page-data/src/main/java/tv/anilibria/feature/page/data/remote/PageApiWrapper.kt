package tv.anilibria.feature.page.data.remote

import toothpick.InjectConstructor
import tv.anilibria.plugin.data.network.ApiWrapper
import tv.anilibria.plugin.data.network.ApiWrapperDeps

@InjectConstructor
class PageApiWrapper(
    apiWrapperDeps: ApiWrapperDeps
) : ApiWrapper<PageApi>(PageApi::class.java, apiWrapperDeps)