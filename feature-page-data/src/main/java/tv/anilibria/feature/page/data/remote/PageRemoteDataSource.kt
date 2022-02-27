package tv.anilibria.feature.page.data.remote

import tv.anilibria.core.types.RelativeUrl
import tv.anilibria.feature.page.data.domain.PageLibria
import tv.anilibria.plugin.data.network.NetworkUrlProvider
import tv.anilibria.plugin.data.network.NetworkWrapper
import javax.inject.Inject

/**
 * Created by radiationx on 13.01.18.
 */
// todo проверить, что при ошибке body не возвращается
class PageRemoteDataSource @Inject constructor(
    private val pagesParser: PagesParser,
    private val urlProvider: NetworkUrlProvider,
    private val pageApi: NetworkWrapper<PageApi>
) {

    suspend fun getLibriaPage(pagePath: RelativeUrl): PageLibria {
        return pageApi.proxy()
            .getBody("${urlProvider.baseUrl}/${pagePath.value}")
            .let { pagesParser.baseParse(it.string()) }
            .toDomain()
    }
}