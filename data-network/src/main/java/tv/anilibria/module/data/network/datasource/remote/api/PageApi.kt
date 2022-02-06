package tv.anilibria.module.data.network.datasource.remote.api

import io.reactivex.Single
import org.json.JSONObject
import tv.anilibria.module.data.network.ApiClient
import tv.anilibria.module.data.network.datasource.remote.ApiResponse
import tv.anilibria.module.data.network.datasource.remote.IClient
import tv.anilibria.module.data.network.datasource.remote.address.ApiConfigProvider
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
    private val apiConfig: ApiConfigProvider
) {
    companion object {
        const val PAGE_PATH_TEAM = "pages/team.php"
        const val PAGE_PATH_DONATE = "pages/donate.php"

        val PAGE_IDS = listOf(
            PAGE_PATH_TEAM,
            PAGE_PATH_DONATE
        )
    }

    fun getPage(pagePath: String): Single<PageLibriaResponse> {
        val args: Map<String, String> = emptyMap()
        return client.get("${apiConfig.baseUrl}/$pagePath", args)
            .map { pagesParser.baseParse(it) }
    }

    fun getComments(): Single<VkCommentsResponse> {
        val args: Map<String, String> = mapOf(
            "query" to "vkcomments"
        )
        return client.post(apiConfig.apiUrl, args)
            .compose(ApiResponse.fetchResult<JSONObject>())
            .map { pagesParser.parseVkComments(it) }
    }
}