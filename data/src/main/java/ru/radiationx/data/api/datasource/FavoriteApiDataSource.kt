package ru.radiationx.data.api.datasource

import io.reactivex.Single
import ru.radiationx.data.adomain.entity.pagination.Paginated
import ru.radiationx.data.adomain.entity.release.Release

interface FavoriteApiDataSource {
    fun getList(page: Int): Single<Paginated<Release>>
    fun add(releaseId: Int): Single<Release>
    fun delete(releaseId: Int): Single<Release>
}