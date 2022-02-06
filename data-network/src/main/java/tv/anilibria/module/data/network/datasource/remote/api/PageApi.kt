package tv.anilibria.module.data.network.datasource.remote.api

import com.squareup.moshi.Moshi
import io.reactivex.Single
import tv.anilibria.module.data.network.ApiClient
import tv.anilibria.module.data.network.datasource.remote.IClient
import tv.anilibria.module.data.network.datasource.remote.address.ApiConfigProvider
import tv.anilibria.module.data.network.datasource.remote.mapApiResponse
import tv.anilibria.module.data.network.datasource.remote.parsers.PagesParser
import tv.anilibria.module.data.network.entity.app.page.VkCommentsResponse
import tv.anilibria.module.data.network.entity.mapper.toDomain
import tv.anilibria.module.domain.entity.page.PageLibria
import tv.anilibria.module.domain.entity.page.VkComments
import javax.inject.Inject

/**
 * Created by radiationx on 13.01.18.
 */
class PageApi @Inject constructor(
    @ApiClient private val client: IClient,
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