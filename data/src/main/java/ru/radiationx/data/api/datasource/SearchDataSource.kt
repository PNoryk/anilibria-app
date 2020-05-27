package ru.radiationx.data.api.datasource

import io.reactivex.Single
import org.json.JSONObject
import ru.radiationx.data.adomain.entity.release.Release
import ru.radiationx.data.adomain.entity.pagination.Paginated
import ru.radiationx.data.api.common.handleApiResponse
import ru.radiationx.data.api.converter.PaginationConverter
import ru.radiationx.data.api.converter.ReleaseConverter
import ru.radiationx.data.api.service.SearchService
import ru.radiationx.data.entity.app.search.SearchForm
import toothpick.InjectConstructor

@InjectConstructor
class SearchDataSource(
    private val searchService: SearchService,
    private val releaseConverter: ReleaseConverter,
    private val paginationConverter: PaginationConverter
) {

    fun getYears(): Single<List<String>> = searchService
        .getYears(mapOf("query" to "years"))
        .handleApiResponse()

    fun getGenres(): Single<List<String>> = searchService
        .getGenres(mapOf("query" to "genres"))
        .handleApiResponse()

    fun getSuggestions(name: String): Single<List<Release>> = searchService
        .getSuggestions(
            mapOf(
                "query" to "search",
                "search" to name,
                "filter" to "id,code,names,poster"
            )
        )
        .handleApiResponse()
        .map {
            it.map { releaseResponse ->
                releaseConverter.toDomain(releaseResponse)
            }
        }

    fun getMatches(form: SearchForm, page: Int): Single<Paginated<Release>> {
        val yearsQuery = form.years?.joinToString(",") { it.value }.orEmpty()
        val seasonsQuery = form.seasons?.joinToString(",") { it.value }.orEmpty()
        val genresQuery = form.genres?.joinToString(",") { it.value }.orEmpty()
        val sortStr = when (form.sort) {
            SearchForm.Sort.RATING -> "2"
            SearchForm.Sort.DATE -> "1"
        }
        val onlyCompletedStr = if (form.onlyCompleted) "2" else "1"

        val params = mapOf(
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

        return searchService
            .getMatches(params)
            .handleApiResponse()
            .map {
                paginationConverter.toDomain(it) { releaseResponse ->
                    releaseConverter.toDomain(releaseResponse)
                }
            }
    }

}