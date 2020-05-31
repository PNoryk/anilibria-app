package ru.radiationx.data.api.datasource

import io.reactivex.Single
import ru.radiationx.data.adomain.entity.feed.Feed
import ru.radiationx.data.adomain.entity.pagination.Paginated

interface FeedApiDataSource {
    fun getList(page: Int): Single<Paginated<Feed>>
}