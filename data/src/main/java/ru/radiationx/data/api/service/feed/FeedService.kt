package ru.radiationx.data.api.service.feed

import io.reactivex.Single
import ru.radiationx.data.adomain.FeedItem
import ru.radiationx.data.adomain.pagination.Paginated
import ru.radiationx.data.api.remote.FeedItemResponse
import ru.radiationx.data.api.common.handleApiResponse
import ru.radiationx.data.api.common.pagination.PaginatedResponse
import ru.radiationx.data.api.converter.FeedConverter
import ru.radiationx.data.api.converter.PaginationConverter

class FeedService(
    private val feedApi: FeedApi,
    private val feedConverter: FeedConverter,
    private val paginationConverter: PaginationConverter
) {

    fun getList(page: Int): Single<Paginated<FeedItem>> = feedApi
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