package tv.anilibria.module.data.network.datasource.remote.api

import com.squareup.moshi.Moshi
import io.reactivex.Single
import tv.anilibria.module.data.network.ApiClient
import tv.anilibria.module.data.network.datasource.remote.IClient
import tv.anilibria.module.data.network.datasource.remote.address.ApiConfigProvider
import tv.anilibria.module.data.network.datasource.remote.mapApiResponse
import tv.anilibria.module.data.network.datasource.remote.parsers.PagesParser
import tv.anilibria.module.data.network.entity.app.page.PageLibriaResponse
import tv.anilibria.module.data.network.entity.app.page.VkCommentsResponse
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

    fun getPage(pagePath: String): Single<PageLibriaResponse> {
        val args: Map<String, String> = emptyMap()
        return client
            .get("${apiConfig.baseUrl}/$pagePath", args)
            .map { pagesParser.baseParse(it) }
    }

    fun getComments(): Single<VkCommentsResponse> {
        val args: Map<String, String> = mapOf(
            "query" to "vkcomments"
        )
        return client
            .post(apiConfig.apiUrl, args)
            .mapApiResponse(moshi)
    }
}