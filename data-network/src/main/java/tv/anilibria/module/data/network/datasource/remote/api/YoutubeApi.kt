package tv.anilibria.module.data.network.datasource.remote.api

import io.reactivex.Single
import org.json.JSONObject
import tv.anilibria.module.data.network.ApiClient
import tv.anilibria.module.data.network.datasource.remote.ApiResponse
import tv.anilibria.module.data.network.datasource.remote.IClient
import tv.anilibria.module.data.network.datasource.remote.address.ApiConfig
import tv.anilibria.module.data.network.datasource.remote.parsers.YoutubeParser
import tv.anilibria.module.data.network.entity.app.Paginated
import tv.anilibria.module.data.network.entity.app.youtube.YoutubeItem
import javax.inject.Inject

class YoutubeApi @Inject constructor(
        @ApiClient private val client: IClient,
        private val youtubeParser: YoutubeParser,
        private val apiConfig: ApiConfig
) {

    fun getYoutubeList(page: Int): Single<Paginated<List<YoutubeItem>>> {
        val args: MutableMap<String, String> = mutableMapOf(
                "query" to "youtube",
                "page" to page.toString()
        )
        return client.post(apiConfig.apiUrl, args)
                .compose(ApiResponse.fetchResult<JSONObject>())
                .map { youtubeParser.parse(it) }
    }
}