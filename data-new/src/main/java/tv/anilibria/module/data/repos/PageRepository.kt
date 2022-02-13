package tv.anilibria.module.data.repos

import tv.anilibria.module.data.restapi.datasource.remote.api.PageRemoteDataSource
import tv.anilibria.module.domain.entity.page.PageLibria
import tv.anilibria.module.domain.entity.page.VkComments
import javax.inject.Inject

/**
 * Created by radiationx on 13.01.18.
 */
class PageRepository @Inject constructor(
    private val pageApi: PageRemoteDataSource
) {

    private var currentComments: VkComments? = null

    suspend fun getPage(pagePath: String): PageLibria {
        return pageApi.getPage(pagePath)
    }

    suspend fun getComments(): VkComments {
        return currentComments ?: pageApi.getComments().also {
            currentComments = it
        }
    }

    suspend fun checkVkBlocked() = pageApi.checkVkBlocked()

}
