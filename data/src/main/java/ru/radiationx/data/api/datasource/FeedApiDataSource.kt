package ru.radiationx.data.api.datasource

import io.reactivex.Single
import anilibria.tv.domain.entity.feed.Feed
import anilibria.tv.domain.entity.pagination.Paginated

interface FeedApiDataSource {
    fun getList(page: Int): Single<Paginated<Feed>>
}