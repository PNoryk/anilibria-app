package tv.anilibria.feature.content.data.remote.datasource.remote.retrofit

import toothpick.InjectConstructor
import tv.anilibria.plugin.data.network.ApiWrapper
import tv.anilibria.plugin.data.network.ApiWrapperDeps

@InjectConstructor
class FavoriteApiWrapper(
    apiWrapperDeps: ApiWrapperDeps
) : ApiWrapper<FavoriteApi>(FavoriteApi::class.java, apiWrapperDeps)