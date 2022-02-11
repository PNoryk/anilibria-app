package tv.anilibria.module.data.restapi.datasource.remote.api

import io.reactivex.Single
import tv.anilibria.module.data.restapi.datasource.remote.retrofit.FeedApi
import tv.anilibria.module.data.restapi.entity.mapper.toDomain
import tv.anilibria.module.domain.entity.feed.Feed
import tv.anilibria.plugin.data.network.formBodyOf
import tv.anilibria.plugin.data.restapi.ApiWrapper
import tv.anilibria.plugin.data.restapi.handleApiResponse
import javax.inject.Inject

class FeedRemoteDataSource @Inject constructor(
    private val feedApi: ApiWrapper<FeedApi>
) {

    fun getFeed(page: Int): Single<List<Feed>> {
        val args = formBodyOf(
            "query" to "feed",
            "page" to page.toString(),
            "filter" to "id,torrents,playlist,favorite,moon,blockedInfo",
            "rm" to "true"
        )
        return feedApi.proxy()
            .getFeed(args)
            .handleApiResponse()
            .map { items -> items.map { it.toDomain() } }
    }

}