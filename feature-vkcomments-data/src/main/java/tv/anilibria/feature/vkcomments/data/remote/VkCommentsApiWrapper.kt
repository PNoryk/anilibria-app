package tv.anilibria.feature.vkcomments.data.remote

import toothpick.InjectConstructor
import tv.anilibria.plugin.data.network.ApiWrapper
import tv.anilibria.plugin.data.network.ApiWrapperDeps

@InjectConstructor
class VkCommentsApiWrapper(
    apiWrapperDeps: ApiWrapperDeps
) : ApiWrapper<VkCommentsApi>(VkCommentsApi::class.java, apiWrapperDeps)