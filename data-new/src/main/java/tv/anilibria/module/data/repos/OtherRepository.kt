package tv.anilibria.module.data.repos

import tv.anilibria.module.data.restapi.datasource.remote.api.OtherRemoteDataSource
import tv.anilibria.module.domain.entity.page.PageLibria
import tv.anilibria.module.domain.entity.page.VkComments
import javax.inject.Inject

/**
 * Created by radiationx on 13.01.18.
 */
class OtherRepository @Inject constructor(
    private val remoteDataSource: OtherRemoteDataSource
) {

    private var currentComments: VkComments? = null

    suspend fun getPage(pagePath: String): PageLibria {
        return remoteDataSource.getLibriaPage(pagePath)
    }

    suspend fun getComments(): VkComments {
        return currentComments ?: remoteDataSource.getComments().also {
            currentComments = it
        }
    }

    suspend fun checkVkBlocked() = remoteDataSource.checkVkBlocked()

    suspend fun getDirectBody(url: String) = remoteDataSource.getDirectBody(url)
}
