package tv.anilibria.module.data.repos

import kotlinx.coroutines.flow.Flow
import tv.anilibria.module.data.local.ReleaseUpdateHelper
import tv.anilibria.module.data.local.holders.GenresLocalDataSource
import tv.anilibria.module.data.local.holders.YearsLocalDataSource
import tv.anilibria.module.data.restapi.datasource.remote.api.SearchRemoteDataSource
import tv.anilibria.module.domain.entity.Page
import tv.anilibria.module.domain.entity.SearchForm
import tv.anilibria.module.domain.entity.release.Release
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val searchApi: SearchRemoteDataSource,
    private val genresHolder: GenresLocalDataSource,
    private val yearsHolder: YearsLocalDataSource,
    private val releaseUpdateHolder: ReleaseUpdateHelper
) {

    fun observeGenres(): Flow<List<String>> {
        return genresHolder.observe()
    }

    fun observeYears(): Flow<List<String>> {
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


    suspend fun getGenres(): List<String> {
        return searchApi.getGenres().also {
            genresHolder.put(it)
        }
    }

    suspend fun getYears(): List<String> {
        return searchApi.getYears().also {
            yearsHolder.put(it)
        }
    }

    fun getSeasons(): List<String> = listOf("зима", "весна", "лето", "осень")

}
