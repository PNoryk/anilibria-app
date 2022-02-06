package tv.anilibria.module.data.network.datasource.remote.api

import android.util.Log
import io.reactivex.Single
import org.json.JSONArray
import tv.anilibria.module.data.network.ApiClient
import tv.anilibria.module.data.network.datasource.remote.ApiResponse
import tv.anilibria.module.data.network.datasource.remote.IClient
import tv.anilibria.module.data.network.datasource.remote.address.ApiConfig
import tv.anilibria.module.data.network.datasource.remote.parsers.FeedParser
import tv.anilibria.module.data.network.datasource.remote.parsers.ReleaseParser
import tv.anilibria.module.data.network.datasource.remote.parsers.YoutubeParser
import tv.anilibria.module.data.network.entity.app.feed.FeedResponse
import javax.inject.Inject

class FeedApi @Inject constructor(
        @ApiClient private val client: IClient,
        private val releaseParser: ReleaseParser,
        private val youtubeParser: YoutubeParser,
        private val feedParser: FeedParser,
        private val apiConfig: ApiConfig
) {

    fun getFeed(page: Int): Single<List<FeedResponse>> {
        val args: MutableMap<String, String> = mutableMapOf(
                "query" to "feed",
                "page" to page.toString(),
                "filter" to "id,torrents,playlist,favorite,moon,blockedInfo",
                "rm" to "true"
        )
        return client.post(apiConfig.apiUrl, args)
                .compose(ApiResponse.fetchResult<JSONArray>())
                .map { feedParser.feed(it, releaseParser, youtubeParser) }
                .doOnError {
                    Log.e("bobobo", "catch error $it")
                }
    }

}