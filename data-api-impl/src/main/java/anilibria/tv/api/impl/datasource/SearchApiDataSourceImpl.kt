package anilibria.tv.api.impl.datasource

import io.reactivex.Single
import anilibria.tv.domain.entity.pagination.Paginated
import anilibria.tv.domain.entity.release.Release
import anilibria.tv.api.impl.common.handleApiResponse
import anilibria.tv.api.impl.converter.PaginationConverter
import anilibria.tv.api.impl.converter.ReleaseConverter
import anilibria.tv.api.SearchApiDataSource
import anilibria.tv.domain.entity.search.SearchForm
import anilibria.tv.api.impl.service.SearchService
import com.google.gson.Gson
import toothpick.InjectConstructor

@InjectConstructor
class SearchApiDataSourceImpl(
    private val searchService: SearchService,
    private val releaseConverter: ReleaseConverter,
    private val paginationConverter: PaginationConverter,
    private val gson: Gson
) : SearchApiDataSource {

    override fun getYears(): Single<List<String>> = searchService
        .getYears(mapOf("query" to "years"))
        .handleApiResponse()

    override fun getGenres(): Single<List<String>> = searchService
        .getGenres(mapOf("query" to "genres"))
        .handleApiResponse()

    override fun getSuggestions(name: String): Single<List<Release>> = searchService
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

    override fun getMatches(form: SearchForm, page: Int): Single<Paginated<Release>> {
        val yearsQuery = form.years?.joinToString(",").orEmpty()
        val seasonsQuery = form.seasons?.joinToString(",").orEmpty()
        val genresQuery = form.genres?.joinToString(",").orEmpty()
        val sortStr = when (form.sort) {
            SearchForm.Sort.RATING -> "2"
            SearchForm.Sort.DATE -> "1"
        }
        val onlyCompletedStr = if (form.onlyCompleted) "2" else "1"

        val searchParams = mapOf(
            "genre" to genresQuery,
            "year" to yearsQuery,
            "season" to seasonsQuery
        )

        val params = mapOf(
            "query" to "catalog",
            "search" to gson.toJson(searchParams),
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