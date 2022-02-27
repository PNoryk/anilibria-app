package tv.anilibria.module.data.restapi.datasource.remote.api

import tv.anilibria.module.data.restapi.datasource.remote.retrofit.FeedApi
import tv.anilibria.module.data.restapi.entity.mapper.toDomain
import tv.anilibria.module.domain.entity.feed.Feed
import tv.anilibria.plugin.data.network.NetworkWrapper
import tv.anilibria.plugin.data.network.formBodyOf
import tv.anilibria.plugin.data.restapi.handleApiResponse
import javax.inject.Inject

class FeedRemoteDataSource @Inject constructor(
    private val feedApi: NetworkWrapper<FeedApi>
) {

    suspend fun getFeed(page: Int): List<Feed> {
        val args = formBodyOf(
            "query" to "feed",
            "page" to page.toString(),
            "filter" to "id,torrents,playlist,favorite,moon,blockedInfo",
            "rm" to "true"
        )
        return feedApi.proxy()
            .getFeed(args)
            .handleApiResponse()
            .map { it.toDomain() }
    }

}