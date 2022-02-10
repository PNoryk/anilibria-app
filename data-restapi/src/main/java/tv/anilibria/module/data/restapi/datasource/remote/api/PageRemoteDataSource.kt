package tv.anilibria.module.data.restapi.datasource.remote.api

import com.squareup.moshi.Moshi
import io.reactivex.Single
import tv.anilibria.plugin.data.network.NetworkClient
import tv.anilibria.module.data.restapi.datasource.remote.parsers.PagesParser
import tv.anilibria.module.data.restapi.entity.app.page.VkCommentsResponse
import tv.anilibria.module.data.restapi.entity.mapper.toDomain
import tv.anilibria.module.domain.entity.page.PageLibria
import tv.anilibria.module.domain.entity.page.VkComments
import tv.anilibria.plugin.data.restapi.ApiClient
import tv.anilibria.plugin.data.restapi.ApiConfigProvider
import tv.anilibria.plugin.data.restapi.mapApiResponse
import javax.inject.Inject

/**
 * Created by radiationx on 13.01.18.
 */
class PageRemoteDataSource @Inject constructor(
    @ApiClient private val client: NetworkClient,
    private val pagesParser: PagesParser,
    private val apiConfig: ApiConfigProvider,
    private val moshi: Moshi
) {

    fun getPage(pagePath: String): Single<PageLibria> {
        return client
            .get("${apiConfig.baseUrl}/$pagePath", emptyMap())
            .map { pagesParser.baseParse(it) }
            .map { it.toDomain() }
    }

    fun getComments(): Single<VkComments> {
        val args = mapOf(
            "query" to "vkcomments"
        )
        return client
            .post(apiConfig.apiUrl, args)
            .mapApiResponse<VkCommentsResponse>(moshi)
            .map { it.toDomain() }
    }
}