package ru.radiationx.data.api.datasource

import io.reactivex.Single
import anilibria.tv.domain.entity.pagination.Paginated
import anilibria.tv.domain.entity.release.Release

interface FavoriteApiDataSource {
    fun getList(page: Int): Single<Paginated<Release>>
    fun add(releaseId: Int): Single<Release>
    fun delete(releaseId: Int): Single<Release>
}