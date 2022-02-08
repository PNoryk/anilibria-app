package tv.anilibria.module.data.restapi.datasource.remote.api

import com.squareup.moshi.Moshi
import io.reactivex.Single
import tv.anilibria.module.data.restapi.ApiClient
import tv.anilibria.module.data.network.NetworkClient
import tv.anilibria.module.data.restapi.datasource.remote.ApiConfigProvider
import tv.anilibria.module.data.restapi.datasource.remote.mapApiResponse
import tv.anilibria.module.data.restapi.datasource.remote.parsers.PagesParser
import tv.anilibria.module.data.restapi.entity.app.page.VkCommentsResponse
import tv.anilibria.module.data.restapi.entity.mapper.toDomain
import tv.anilibria.module.domain.entity.page.PageLibria
import tv.anilibria.module.domain.entity.page.VkComments
import tv.anilibria.module.domain.remote.PageRemoteDataSource
import javax.inject.Inject

/**
 * Created by radiationx on 13.01.18.
 */
class PageRemoteDataSourceImpl @Inject constructor(
    @ApiClient private val client: NetworkClient,
    private val pagesParser: PagesParser,
    private val apiConfig: ApiConfigProvider,
    private val moshi: Moshi
) : PageRemoteDataSource {

    override fun getPage(pagePath: String): Single<PageLibria> {
        return client
            .get("${apiConfig.baseUrl}/$pagePath", emptyMap())
            .map { pagesParser.baseParse(it) }
            .map { it.toDomain() }
    }

    override fun getComments(): Single<VkComments> {
        val args = mapOf(
            "query" to "vkcomments"
        )
        return client
            .post(apiConfig.apiUrl, args)
            .mapApiResponse<VkCommentsResponse>(moshi)
            .map { it.toDomain() }
    }
}