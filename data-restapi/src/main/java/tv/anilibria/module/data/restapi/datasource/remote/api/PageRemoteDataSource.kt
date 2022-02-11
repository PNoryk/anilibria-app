package tv.anilibria.module.data.restapi.datasource.remote.api

import io.reactivex.Single
import tv.anilibria.module.data.restapi.datasource.remote.parsers.PagesParser
import tv.anilibria.module.data.restapi.datasource.remote.retrofit.OtherApi
import tv.anilibria.module.data.restapi.entity.mapper.toDomain
import tv.anilibria.module.domain.entity.page.PageLibria
import tv.anilibria.module.domain.entity.page.VkComments
import tv.anilibria.plugin.data.network.formBodyOf
import tv.anilibria.plugin.data.restapi.ApiConfigProvider
import tv.anilibria.plugin.data.restapi.ApiNetworkClient
import tv.anilibria.plugin.data.restapi.ApiWrapper
import tv.anilibria.plugin.data.restapi.handleApiResponse
import javax.inject.Inject

/**
 * Created by radiationx on 13.01.18.
 */
class PageRemoteDataSource @Inject constructor(
    private val apiClient: ApiNetworkClient,
    private val pagesParser: PagesParser,
    private val apiConfig: ApiConfigProvider,
    private val otherApi: ApiWrapper<OtherApi>
) {

    fun getPage(pagePath: String): Single<PageLibria> {
        return apiClient
            .get("${apiConfig.baseUrl}/$pagePath", emptyMap())
            .map { pagesParser.baseParse(it.body) }
            .map { it.toDomain() }
    }

    fun getComments(): Single<VkComments> {
        val args = formBodyOf(
            "query" to "vkcomments"
        )
        return otherApi.proxy()
            .getVkComments(args)
            .handleApiResponse()
            .map { it.toDomain() }
    }
}