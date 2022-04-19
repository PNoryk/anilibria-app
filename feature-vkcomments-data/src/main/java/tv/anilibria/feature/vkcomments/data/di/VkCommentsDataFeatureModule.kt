package tv.anilibria.feature.vkcomments.data.di

import toothpick.config.Module
import tv.anilibria.feature.vkcomments.data.VkCommentsRepository
import tv.anilibria.feature.vkcomments.data.remote.VkCommentsApiWrapper
import tv.anilibria.feature.vkcomments.data.remote.VkCommentsRemoteDataSource

class VkCommentsDataFeatureModule : Module() {

    init {
        bind(VkCommentsApiWrapper::class.java)
        bind(VkCommentsRemoteDataSource::class.java)
        bind(VkCommentsRepository::class.java)
    }
}