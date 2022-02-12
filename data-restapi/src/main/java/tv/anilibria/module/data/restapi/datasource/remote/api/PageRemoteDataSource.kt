package tv.anilibria.module.data.restapi.datasource.remote.api

import tv.anilibria.module.data.restapi.datasource.remote.parsers.PagesParser
import tv.anilibria.module.data.restapi.datasource.remote.retrofit.OtherApi
import tv.anilibria.module.data.restapi.entity.mapper.toDomain
import tv.anilibria.module.domain.entity.page.PageLibria
import tv.anilibria.module.domain.entity.page.VkComments
import tv.anilibria.plugin.data.network.formBodyOf
import tv.anilibria.plugin.data.restapi.ApiWrapper
import tv.anilibria.plugin.data.restapi.NetworkUrlProvider
import tv.anilibria.plugin.data.restapi.handleApiResponse
import javax.inject.Inject

/**
 * Created by radiationx on 13.01.18.
 */
class PageRemoteDataSource @Inject constructor(
    private val pagesParser: PagesParser,
    private val urlProvider: NetworkUrlProvider,
    private val otherApi: ApiWrapper<OtherApi>
) {

    suspend fun getPage(pagePath: String): PageLibria {
        return otherApi.proxy()
            .getLibriaPage("${urlProvider.baseUrl}/$pagePath")
            .let { pagesParser.baseParse(it.string()) }
            .toDomain()
    }

    suspend fun getComments(): VkComments {
        val args = formBodyOf(
            "query" to "vkcomments"
        )
        return otherApi.proxy()
            .getVkComments(args)
            .handleApiResponse()
            .toDomain()
    }
}