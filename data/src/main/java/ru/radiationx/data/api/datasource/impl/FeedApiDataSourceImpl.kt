package ru.radiationx.data.api.datasource.impl

import io.reactivex.Single
import anilibria.tv.domain.entity.feed.Feed
import anilibria.tv.domain.entity.pagination.Paginated
import ru.radiationx.data.api.common.handleApiResponse
import ru.radiationx.data.api.converter.FeedConverter
import ru.radiationx.data.api.converter.PaginationConverter
import ru.radiationx.data.api.datasource.FeedApiDataSource
import ru.radiationx.data.api.service.FeedService
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