package tv.anilibria.feature.content.data.remote.datasource.remote.api

import toothpick.InjectConstructor
import tv.anilibria.feature.content.data.remote.datasource.remote.retrofit.FeedApi
import tv.anilibria.feature.content.data.remote.entity.mapper.toDomain
import tv.anilibria.feature.content.types.feed.Feed
import tv.anilibria.plugin.data.network.ApiWrapper
import tv.anilibria.plugin.data.network.formBodyOf
import tv.anilibria.plugin.data.restapi.handleApiResponse

@InjectConstructor
class FeedRemoteDataSource(
    private val feedApi: ApiWrapper<FeedApi>
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