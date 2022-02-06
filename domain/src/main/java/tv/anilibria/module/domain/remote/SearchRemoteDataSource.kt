package tv.anilibria.module.domain.remote

import io.reactivex.Single
import tv.anilibria.module.domain.entity.Page
import tv.anilibria.module.domain.entity.release.Release

interface SearchRemoteDataSource {
    fun getGenres(): Single<List<String>>
    fun getYears(): Single<List<String>>
    fun fastSearch(name: String): Single<List<Release>>
    fun searchReleases(
        genre: String,
        year: String,
        season: String,
        sort: String,
        complete: String,
        page: Int
    ): Single<Page<Release>>
}