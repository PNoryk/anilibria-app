package tv.anilibria.feature.user.data.remote

import toothpick.InjectConstructor
import tv.anilibria.plugin.data.network.ApiWrapper
import tv.anilibria.plugin.data.network.ApiWrapperDeps

@InjectConstructor
class UserApiWrapper(
    apiWrapperDeps: ApiWrapperDeps
) : ApiWrapper<UserApi>(UserApi::class.java, apiWrapperDeps)