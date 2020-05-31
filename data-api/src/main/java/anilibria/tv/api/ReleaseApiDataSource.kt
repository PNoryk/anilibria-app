package anilibria.tv.api

import io.reactivex.Single
import anilibria.tv.domain.entity.pagination.Paginated
import anilibria.tv.domain.entity.release.RandomRelease
import anilibria.tv.domain.entity.release.Release

interface ReleaseApiDataSource {
    fun getOne(releaseId: Int? = null, releaseCode: String? = null): Single<Release>
    fun getSome(ids: List<Int>? = null, codes: List<String>? = null): Single<List<Release>>
    fun getList(page: Int): Single<Paginated<Release>>
    fun getRandom(): Single<RandomRelease>
}