package tv.anilibria.feature.vkcomments.data

import tv.anilibria.core.types.AbsoluteUrl
import tv.anilibria.feature.vkcomments.data.remote.VkCommentsRemoteDataSource
import tv.anilibria.module.domain.entity.page.VkComments
import javax.inject.Inject

/**
 * Created by radiationx on 13.01.18.
 */
class VkCommentsRepository @Inject constructor(
    private val remoteDataSource: VkCommentsRemoteDataSource
) {

    private var currentComments: VkComments? = null

    suspend fun getComments(): VkComments {
        return currentComments ?: remoteDataSource.getComments().also {
            currentComments = it
        }
    }

    suspend fun checkVkBlocked() = remoteDataSource.checkVkBlocked()

    suspend fun getDirectBody(url: AbsoluteUrl) = remoteDataSource.getDirectBody(url)
}
