package anilibria.tv.api.impl.datasource

import anilibria.tv.api.impl.converter.PaginationConverter
import anilibria.tv.api.impl.converter.ReleaseConverter
import anilibria.tv.api.impl.converter.SearchFormConverter
import anilibria.tv.api.impl.converter.YoutubeConverter
import anilibria.tv.api.impl.entity.common.ApiBaseResponse
import anilibria.tv.api.impl.entity.pagination.PaginatedResponse
import anilibria.tv.api.impl.entity.release.ReleaseResponse
import anilibria.tv.api.impl.entity.search.SearchFormRequest
import anilibria.tv.api.impl.entity.youtube.YouTubeResponse
import anilibria.tv.api.impl.service.*
import anilibria.tv.domain.entity.pagination.Paginated
import anilibria.tv.domain.entity.release.Release
import anilibria.tv.domain.entity.search.SearchForm
import anilibria.tv.domain.entity.youtube.Youtube
import com.google.gson.Gson
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.Test

class SearchApiDataSourceTest {

    private val service = mockk<SearchService>()
    private val formConverter = mockk<SearchFormConverter>()
    private val releaseConverter = mockk<ReleaseConverter>()
    private val paginationConverter = mockk<PaginationConverter>()
    private val gson = Gson()
    private val dataSource = SearchApiDataSourceImpl(service, formConverter, releaseConverter, paginationConverter, gson)

    @Test
    fun `get years EXPECT success`() {
        val params = mapOf("query" to "years")
        val response = listOf("year1", "year2")
        val domain = listOf("year1", "year2")
        val apiResponse = ApiBaseResponse(true, response, null)

        every { service.getYears(params) } returns Single.just(apiResponse)

        dataSource.getYears().test().assertValue(domain)

        verify { service.getYears(params) }
        confirmVerified(service, formConverter, releaseConverter, paginationConverter)
    }

    @Test
    fun `get genres EXPECT success`() {
        val params = mapOf("query" to "genres")
        val response = listOf("genre1", "genre2")
        val domain = listOf("genre1", "genre2")
        val apiResponse = ApiBaseResponse(true, response, null)

        every { service.getGenres(params) } returns Single.just(apiResponse)

        dataSource.getGenres().test().assertValue(domain)

        verify { service.getGenres(params) }
        confirmVerified(service, formConverter, releaseConverter, paginationConverter)
    }

    @Test
    fun `get suggestions EXPECT success`() {
        val params = mapOf(
            "query" to "search",
            "search" to "somename",
            "filter" to "id,code,names,poster"
        )
        val response = listOf<ReleaseResponse>(mockk(), mockk())
        val domain = listOf<Release>(mockk(), mockk())
        val apiResponse = ApiBaseResponse(true, response, null)

        every { service.getSuggestions(params) } returns Single.just(apiResponse)
        every { releaseConverter.toDomain(response[0]) } returns domain[0]
        every { releaseConverter.toDomain(response[1]) } returns domain[1]

        dataSource.getSuggestions("somename").test().assertValue(domain)

        verify { service.getSuggestions(params) }
        verify { releaseConverter.toDomain(response[0]) }
        verify { releaseConverter.toDomain(response[1]) }
        confirmVerified(service, formConverter, releaseConverter, paginationConverter)
    }

    @Test
    fun `get matches EXPECT success`() {
        val params = mapOf(
            "query" to "catalog",
            "search" to """{"genre":"genre1,genre2,genre3","year":"year1,year2","season":"season1"}""",
            "finish" to "1",
            "xpage" to "catalog",
            "sort" to "2",
            "page" to "1",
            "filter" to "id,torrents,playlist,favorite,moon,blockedInfo",
            "rm" to "true"
        )
        val searchForm = mockk<SearchForm>()
        val searchFormRequest = SearchFormRequest("year1,year2", "season1", "genre1,genre2,genre3", "2", "1")
        val response = PaginatedResponse<ReleaseResponse>(listOf(mockk(), mockk()), mockk())
        val domain = Paginated<Release>(listOf(mockk(), mockk()), mockk())
        val apiResponse = ApiBaseResponse(true, response, null)

        every { service.getMatches(params) } returns Single.just(apiResponse)
        every { formConverter.toRequest(searchForm) } returns searchFormRequest
        every { paginationConverter.toDomain(response, any<((ReleaseResponse) -> Release)>()) } returns domain

        dataSource.getMatches(searchForm, 1).test().assertValue(domain)

        verify { formConverter.toRequest(searchForm) }
        verify { service.getMatches(params) }
        verify { paginationConverter.toDomain(response, any<((ReleaseResponse) -> Release)>()) }
        confirmVerified(service, formConverter, releaseConverter, paginationConverter)
    }
}