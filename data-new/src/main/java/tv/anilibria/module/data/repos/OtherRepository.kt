package tv.anilibria.module.data.repos

import tv.anilibria.core.types.RelativeUrl
import tv.anilibria.module.data.restapi.datasource.remote.api.OtherRemoteDataSource
import tv.anilibria.module.domain.entity.page.PageLibria
import javax.inject.Inject

/**
 * Created by radiationx on 13.01.18.
 */
class OtherRepository @Inject constructor(
    private val remoteDataSource: OtherRemoteDataSource
) {

    suspend fun getPage(pagePath: RelativeUrl): PageLibria {
        return remoteDataSource.getLibriaPage(pagePath)
    }
}
