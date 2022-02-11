package tv.anilibria.module.data.restapi.datasource.remote.api

import com.squareup.moshi.Moshi
import io.reactivex.Single
import tv.anilibria.module.data.restapi.entity.app.feed.FeedResponse
import tv.anilibria.module.data.restapi.entity.mapper.toDomain
import tv.anilibria.module.domain.entity.feed.Feed
import tv.anilibria.plugin.data.restapi.ApiNetworkClient
import tv.anilibria.plugin.data.restapi.ApiConfigProvider
import tv.anilibria.plugin.data.restapi.mapApiResponse
import javax.inject.Inject

class FeedRemoteDataSource @Inject constructor(
    private val apiClient: ApiNetworkClient,
    private val apiConfig: ApiConfigProvider,
    private val moshi: Moshi
) {

    fun getFeed(page: Int): Single<List<Feed>> {
        val args = mapOf(
            "query" to "feed",
            "page" to page.toString(),
            "filter" to "id,torrents,playlist,favorite,moon,blockedInfo",
            "rm" to "true"
        )
        return apiClient
            .post(apiConfig.apiUrl, args)
            .mapApiResponse<List<FeedResponse>>(moshi)
            .map { items -> items.map { it.toDomain() } }
    }

}