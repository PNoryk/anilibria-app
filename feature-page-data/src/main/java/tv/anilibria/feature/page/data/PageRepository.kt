package tv.anilibria.feature.page.data

import tv.anilibria.core.types.RelativeUrl
import tv.anilibria.feature.page.data.remote.PageRemoteDataSource
import tv.anilibria.module.domain.entity.page.PageLibria
import javax.inject.Inject

/**
 * Created by radiationx on 13.01.18.
 */
class PageRepository @Inject constructor(
    private val remoteDataSource: PageRemoteDataSource
) {

    suspend fun getPage(pagePath: RelativeUrl): PageLibria {
        return remoteDataSource.getLibriaPage(pagePath)
    }
}
