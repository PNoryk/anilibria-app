package anilibria.tv.api.impl.datasource

import io.reactivex.Single
import anilibria.tv.domain.entity.feed.Feed
import anilibria.tv.domain.entity.pagination.Paginated
import anilibria.tv.api.impl.common.handleApiResponse
import anilibria.tv.api.impl.converter.FeedConverter
import anilibria.tv.api.impl.converter.PaginationConverter
import anilibria.tv.api.FeedApiDataSource
import anilibria.tv.api.impl.service.FeedService
import toothpick.InjectConstructor

@InjectConstructor
class FeedApiDataSourceImpl(
    private val feedService: FeedService,
    private val feedConverter: FeedConverter,
    private val paginationConverter: PaginationConverter
) : FeedApiDataSource {

    override fun getList(page: Int): Single<Paginated<Feed>> = feedService
        .getList(
            mapOf(
                "query" to "feed",
                "page" to page.toString(),
                "filter" to "id,torrents,playlist,favorite,moon,blockedInfo",
                "rm" to "true"
            )
        )
        .handleApiResponse()
        .map {
            paginationConverter.toDomain(it) { feedItemResponse ->
                feedConverter.toDomain(feedItemResponse)
            }
        }
}