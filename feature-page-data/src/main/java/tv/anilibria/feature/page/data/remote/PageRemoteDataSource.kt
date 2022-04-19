package tv.anilibria.feature.page.data.remote

import toothpick.InjectConstructor
import tv.anilibria.core.types.RelativeUrl
import tv.anilibria.feature.page.data.domain.PageLibria
import tv.anilibria.plugin.data.network.NetworkUrlProvider

// todo проверить, что при ошибке body не возвращается
@InjectConstructor
class PageRemoteDataSource(
    private val pagesParser: PagesParser,
    private val urlProvider: NetworkUrlProvider,
    private val pageApi: PageApiWrapper
) {

    suspend fun getLibriaPage(pagePath: RelativeUrl): PageLibria {
        return pageApi.proxy()
            .getBody("${urlProvider.baseUrl}/${pagePath.value}")
            .let { pagesParser.baseParse(it.string()) }
            .toDomain()
    }
}