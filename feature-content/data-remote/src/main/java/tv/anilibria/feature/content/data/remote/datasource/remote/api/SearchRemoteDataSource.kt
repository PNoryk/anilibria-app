package tv.anilibria.feature.content.data.remote.datasource.remote.api

import org.json.JSONObject
import tv.anilibria.feature.content.data.remote.datasource.remote.retrofit.SearchApi
import tv.anilibria.feature.content.data.remote.entity.mapper.toDomain
import tv.anilibria.feature.domain.entity.Page
import tv.anilibria.feature.domain.entity.ReleaseGenre
import tv.anilibria.feature.domain.entity.ReleaseYear
import tv.anilibria.feature.domain.entity.SearchForm
import tv.anilibria.feature.domain.entity.release.Release
import tv.anilibria.plugin.data.network.NetworkWrapper
import tv.anilibria.plugin.data.network.formBodyOf
import tv.anilibria.plugin.data.restapi.handleApiResponse
import javax.inject.Inject

class SearchRemoteDataSource @Inject constructor(
    private val searchApi: NetworkWrapper<SearchApi>
) {

    suspend fun getGenres(): List<ReleaseGenre> {
        val args = formBodyOf(
            "query" to "genres"
        )
        return searchApi.proxy()
            .getGenres(args)
            .handleApiResponse()
            .map { ReleaseGenre(it) }
    }

    suspend fun getYears(): List<ReleaseYear> {
        val args = formBodyOf(
            "query" to "years"
        )
        return searchApi.proxy()
            .getYears(args)
            .handleApiResponse()
            .map { ReleaseYear(it) }
    }

    suspend fun fastSearch(name: String): List<Release> {
        val args = formBodyOf(
            "query" to "search",
            "search" to name,
            "filter" to "id,code,names,poster"
        )
        return searchApi.proxy()
            .fastSearch(args)
            .handleApiResponse()
            .map { it.toDomain() }
    }

    suspend fun searchReleases(
        form: SearchForm,
        page: Int
    ): Page<Release> {
        val yearsQuery = form.years?.joinToString(",") { it.value }.orEmpty()
        val seasonsQuery = form.seasons?.joinToString(",") { it.value }.orEmpty()
        val genresQuery = form.genres?.joinToString(",") { it.value }.orEmpty()
        val sortStr = when (form.sort) {
            SearchForm.Sort.RATING -> "2"
            SearchForm.Sort.DATE -> "1"
        }
        val onlyCompletedStr = if (form.onlyCompleted) "2" else "1"

        val args = formBodyOf(
            "query" to "catalog",
            "search" to JSONObject().apply {
                put("genre", genresQuery)
                put("year", yearsQuery)
                put("season", seasonsQuery)
            }.toString(),
            "finish" to onlyCompletedStr,
            "xpage" to "catalog",
            "sort" to sortStr,
            "page" to page.toString(),
            "filter" to "id,torrents,playlist,favorite,moon,blockedInfo",
            "rm" to "true"
        )
        return searchApi.proxy()
            .search(args)
            .handleApiResponse()
            .toDomain { it.toDomain() }
    }

}
