package tv.anilibria.module.data.network.datasource.remote.api

import io.reactivex.Single
import tv.anilibria.module.domain.entity.Page
import tv.anilibria.module.domain.entity.release.RandomRelease
import tv.anilibria.module.domain.entity.release.Release

interface ReleaseRemoteDataSource {
    fun getRandomRelease(): Single<RandomRelease>
    fun getRelease(releaseId: Int): Single<Release>
    fun getRelease(releaseCode: String): Single<Release>
    fun getReleasesByIds(ids: List<Int>): Single<List<Release>>
    fun getReleases(page: Int): Single<Page<Release>>
}