package ru.radiationx.data.api.datasource

import io.reactivex.Single
import ru.radiationx.data.adomain.entity.feed.Feed
import ru.radiationx.data.adomain.entity.pagination.Paginated
import ru.radiationx.data.api.common.handleApiResponse
import ru.radiationx.data.api.converter.FeedConverter
import ru.radiationx.data.api.converter.PaginationConverter
import ru.radiationx.data.api.service.FeedService
import toothpick.InjectConstructor

@InjectConstructor
class FeedApiDataSourceImpl(
    private val feedService: FeedService,
    private val feedConverter: FeedConverter,
    private val paginationConverter: PaginationConverter
) {

    fun getList(page: Int): Single<Paginated<Feed>> = feedService
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