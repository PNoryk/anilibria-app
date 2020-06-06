package anilibria.tv.api.impl.datasource

import io.reactivex.Single
import anilibria.tv.domain.entity.pagination.Paginated
import anilibria.tv.domain.entity.release.Release
import anilibria.tv.api.impl.common.handleApiResponse
import anilibria.tv.api.impl.converter.PaginationConverter
import anilibria.tv.api.impl.converter.ReleaseConverter
import anilibria.tv.api.SearchApiDataSource
import anilibria.tv.api.impl.converter.SearchFormConverter
import anilibria.tv.domain.entity.search.SearchForm
import anilibria.tv.api.impl.service.SearchService
import com.google.gson.Gson
import toothpick.InjectConstructor

@InjectConstructor
class SearchApiDataSourceImpl(
    private val searchService: SearchService,
    private val searchFormConverter: SearchFormConverter,
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

    override fun getMatches(form: SearchForm, page: Int): Single<Paginated<Release>> = Single
        .defer {
            val formRequest = searchFormConverter.toRequest(form)

            val searchParams = linkedMapOf(
                "genre" to formRequest.genres,
                "year" to formRequest.years,
                "season" to formRequest.seasons
            )

            val params = mapOf(
                "query" to "catalog",
                "search" to gson.toJson(searchParams),
                "finish" to formRequest.onlyCompleted,
                "xpage" to "catalog",
                "sort" to formRequest.sort,
                "page" to page.toString(),
                "filter" to "id,torrents,playlist,favorite,moon,blockedInfo",
                "rm" to "true"
            )
            searchService.getMatches(params)
        }
        .handleApiResponse()
        .map {
            paginationConverter.toDomain(it) { releaseResponse ->
                releaseConverter.toDomain(releaseResponse)
            }
        }

}