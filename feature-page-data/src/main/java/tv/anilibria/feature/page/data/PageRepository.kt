package tv.anilibria.feature.page.data

import toothpick.InjectConstructor
import tv.anilibria.core.types.RelativeUrl
import tv.anilibria.feature.page.data.domain.PageLibria
import tv.anilibria.feature.page.data.remote.PageRemoteDataSource

@InjectConstructor
class PageRepository(
    private val remoteDataSource: PageRemoteDataSource
) {

    suspend fun getPage(pagePath: RelativeUrl): PageLibria {
        return remoteDataSource.getLibriaPage(pagePath)
    }
}
