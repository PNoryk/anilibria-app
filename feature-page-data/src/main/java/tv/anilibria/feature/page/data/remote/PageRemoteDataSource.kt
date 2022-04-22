package tv.anilibria.feature.page.data.remote

import toothpick.InjectConstructor
import tv.anilibria.core.types.RelativeUrl
import tv.anilibria.feature.page.data.domain.PageLibria
import tv.anilibria.plugin.data.network.BaseUrlsProvider

// todo проверить, что при ошибке body не возвращается
@InjectConstructor
class PageRemoteDataSource(
    private val pagesParser: PagesParser,
    private val urlProvider: BaseUrlsProvider,
    private val pageApi: PageApiWrapper
) {

    suspend fun getLibriaPage(pagePath: RelativeUrl): PageLibria {
        return pageApi.proxy()
            .getBody("${urlProvider.base.value}/${pagePath.value}")
            .let { pagesParser.baseParse(it.string()) }
            .toDomain()
    }
}