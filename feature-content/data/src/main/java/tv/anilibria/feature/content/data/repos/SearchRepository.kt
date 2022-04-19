package tv.anilibria.feature.content.data.repos

import kotlinx.coroutines.flow.Flow
import toothpick.InjectConstructor
import tv.anilibria.feature.content.data.local.ReleaseUpdateHelper
import tv.anilibria.feature.content.data.local.holders.GenresLocalDataSource
import tv.anilibria.feature.content.data.local.holders.YearsLocalDataSource
import tv.anilibria.feature.content.data.remote.datasource.remote.api.SearchRemoteDataSource
import tv.anilibria.feature.content.types.*
import tv.anilibria.feature.content.types.release.Release

@InjectConstructor
class SearchRepository(
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
