package tv.anilibria.feature.content.data.remote.datasource.remote.api

import org.json.JSONObject
import toothpick.InjectConstructor
import tv.anilibria.feature.content.data.remote.datasource.remote.retrofit.SearchApiWrapper
import tv.anilibria.feature.content.data.remote.entity.mapper.toDomain
import tv.anilibria.feature.content.types.Page
import tv.anilibria.feature.content.types.ReleaseGenre
import tv.anilibria.feature.content.types.ReleaseYear
import tv.anilibria.feature.content.types.SearchForm
import tv.anilibria.feature.content.types.release.Release
import tv.anilibria.plugin.data.network.BaseUrlsProvider
import tv.anilibria.plugin.data.network.formBodyOf
import tv.anilibria.plugin.data.restapi.handleApiResponse

@InjectConstructor
class SearchRemoteDataSource(
    private val searchApi: SearchApiWrapper,
    private val urlsProvider: BaseUrlsProvider
) {

    suspend fun getGenres(): List<ReleaseGenre> {
        val args = formBodyOf(
            "query" to "genres"
        )
        return searchApi.proxy()
            .getGenres(urlsProvider.api.value, args)
            .handleApiResponse()
            .map { ReleaseGenre(it) }
    }

    suspend fun getYears(): List<ReleaseYear> {
        val args = formBodyOf(
            "query" to "years"
        )
        return searchApi.proxy()
            .getYears(urlsProvider.api.value, args)
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
            .fastSearch(urlsProvider.api.value, args)
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
            .search(urlsProvider.api.value, args)
            .handleApiResponse()
            .toDomain { it.toDomain() }
    }

}
