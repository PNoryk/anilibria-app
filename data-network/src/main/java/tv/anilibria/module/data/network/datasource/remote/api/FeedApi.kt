package tv.anilibria.module.data.network.datasource.remote.api

import com.squareup.moshi.Moshi
import io.reactivex.Single
import tv.anilibria.module.data.network.ApiClient
import tv.anilibria.module.data.network.datasource.remote.IClient
import tv.anilibria.module.data.network.datasource.remote.address.ApiConfigProvider
import tv.anilibria.module.data.network.datasource.remote.mapApiResponse
import tv.anilibria.module.data.network.entity.app.feed.FeedResponse
import tv.anilibria.module.data.network.entity.mapper.toDomain
import tv.anilibria.module.domain.entity.feed.Feed
import javax.inject.Inject

class FeedApi @Inject constructor(
    @ApiClient private val client: IClient,
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
        return client
            .post(apiConfig.apiUrl, args)
            .mapApiResponse<List<FeedResponse>>(moshi)
            .map { items -> items.map { it.toDomain() } }
    }

}