package tv.anilibria.module.data.repos

import kotlinx.coroutines.flow.Flow
import tv.anilibria.module.data.local.ReleaseUpdateHelper
import tv.anilibria.module.data.local.holders.GenresLocalDataSource
import tv.anilibria.module.data.local.holders.YearsLocalDataSource
import tv.anilibria.module.data.restapi.datasource.remote.api.SearchRemoteDataSource
import tv.anilibria.module.domain.entity.*
import tv.anilibria.module.domain.entity.release.Release
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val searchApi: SearchRemoteDataSource,
    private val genresHolder: GenresLocalDataSource,
    private val yearsHolder: YearsLocalDataSource,
    private val releaseUpdateHolder: ReleaseUpdateHelper
) {

    fun observeGenres(): Flow<List<ReleaseGenre>> {
        return genresHolder.observe()
    }

    fun observeYears(): Flow<List<ReleaseYear>> {
        return yearsHolder.observe()
    }

    suspend fun fastSearch(query: String): List<Release> {
        return searchApi.fastSearch(query).also {
            releaseUpdateHolder.update(it)
        }
    }

    suspend fun searchReleases(form: SearchForm, page: Int): Page<Release> {
        return searchApi.searchReleases(form, page).also {
            releaseUpdateHolder.update(it.items)
        }
    }

    suspend fun getGenres(): List<ReleaseGenre> {
        return searchApi.getGenres().also {
            genresHolder.put(it)
        }
    }

    suspend fun getYears(): List<ReleaseYear> {
        return searchApi.getYears().also {
            yearsHolder.put(it)
        }
    }

    fun getSeasons(): List<ReleaseSeason> =
        listOf("зима", "весна", "лето", "осень").map { ReleaseSeason(it) }

}
