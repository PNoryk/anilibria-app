package ru.radiationx.data.api.datasource

import io.reactivex.Single
import anilibria.tv.domain.entity.pagination.Paginated
import anilibria.tv.domain.entity.youtube.Youtube

interface YouTubeApiDataSource {
    fun getList(page: Int): Single<Paginated<Youtube>>
}