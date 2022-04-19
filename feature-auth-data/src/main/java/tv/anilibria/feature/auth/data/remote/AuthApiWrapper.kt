package tv.anilibria.feature.auth.data.remote

import toothpick.InjectConstructor
import tv.anilibria.plugin.data.network.ApiWrapper
import tv.anilibria.plugin.data.network.ApiWrapperDeps

@InjectConstructor
class AuthApiWrapper(
    apiWrapperDeps: ApiWrapperDeps
) : ApiWrapper<AuthApi>(AuthApi::class.java, apiWrapperDeps)