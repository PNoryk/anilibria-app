package tv.anilibria.module.data.restapi.datasource.remote.api

import tv.anilibria.core.types.RelativeUrl
import tv.anilibria.module.data.restapi.datasource.remote.parsers.PagesParser
import tv.anilibria.module.data.restapi.datasource.remote.retrofit.OtherApi
import tv.anilibria.module.data.restapi.entity.mapper.toDomain
import tv.anilibria.module.domain.entity.page.PageLibria
import tv.anilibria.plugin.data.network.NetworkUrlProvider
import tv.anilibria.plugin.data.network.NetworkWrapper
import javax.inject.Inject

/**
 * Created by radiationx on 13.01.18.
 */
// todo проверить, что при ошибке body не возвращается
class OtherRemoteDataSource @Inject constructor(
    private val pagesParser: PagesParser,
    private val urlProvider: NetworkUrlProvider,
    private val otherApi: NetworkWrapper<OtherApi>
) {

    suspend fun getLibriaPage(pagePath: RelativeUrl): PageLibria {
        return otherApi.proxy()
            .getBody("${urlProvider.baseUrl}/${pagePath.value}")
            .let { pagesParser.baseParse(it.string()) }
            .toDomain()
    }
}