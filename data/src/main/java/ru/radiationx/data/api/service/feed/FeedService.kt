package ru.radiationx.data.api.service.feed

import io.reactivex.Single
import ru.radiationx.data.api.remote.FeedItemResponse
import ru.radiationx.data.api.common.handleApiResponse
import ru.radiationx.data.api.common.pagination.PaginatedResponse

class FeedService(
    private val feedApi: FeedApi
) {

    fun getList(page: Int): Single<PaginatedResponse<FeedItemResponse>> = feedApi
        .getList(
            mapOf(
                "query" to "feed",
                "page" to page.toString(),
                "filter" to "id,torrents,playlist,favorite,moon,blockedInfo",
                "rm" to "true"
            )
        )
        .handleApiResponse()
}