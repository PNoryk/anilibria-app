package ru.radiationx.data.api.datasource

import io.reactivex.Single
import ru.radiationx.data.adomain.entity.pagination.Paginated
import ru.radiationx.data.adomain.entity.release.RandomRelease
import ru.radiationx.data.adomain.entity.release.Release

interface ReleaseApiDataSource {
    fun getOne(releaseId: Int? = null, releaseCode: String? = null): Single<Release>
    fun getSome(ids: List<Int>? = null, codes: List<String>? = null): Single<List<Release>>
    fun getList(page: Int): Single<Paginated<Release>>
    fun getRandom(): Single<RandomRelease>
}