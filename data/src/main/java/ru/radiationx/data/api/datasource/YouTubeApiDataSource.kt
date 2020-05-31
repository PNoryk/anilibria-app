package ru.radiationx.data.api.datasource

import io.reactivex.Single
import ru.radiationx.data.adomain.entity.pagination.Paginated
import ru.radiationx.data.adomain.entity.youtube.Youtube

interface YouTubeApiDataSource {
    fun getList(page: Int): Single<Paginated<Youtube>>
}