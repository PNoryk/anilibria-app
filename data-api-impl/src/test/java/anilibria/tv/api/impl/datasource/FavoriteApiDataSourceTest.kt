package anilibria.tv.api.impl.datasource

import anilibria.tv.api.impl.converter.FeedConverter
import anilibria.tv.api.impl.converter.PaginationConverter
import anilibria.tv.api.impl.converter.ReleaseConverter
import anilibria.tv.api.impl.entity.common.ApiBaseResponse
import anilibria.tv.api.impl.entity.feed.FeedItemResponse
import anilibria.tv.api.impl.entity.pagination.PaginatedResponse
import anilibria.tv.api.impl.entity.release.ReleaseResponse
import anilibria.tv.api.impl.service.*
import anilibria.tv.domain.entity.feed.Feed
import anilibria.tv.domain.entity.pagination.Paginated
import anilibria.tv.domain.entity.release.Release
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.Test

class FavoriteApiDataSourceTest {

    private val service = mockk<FavoriteService>()
    private val releaseConverter = mockk<ReleaseConverter>()
    private val paginationConverter = mockk<PaginationConverter>()
    private val dataSource = FavoriteApiDataSourceImpl(service, releaseConverter, paginationConverter)


    @Test
    fun `get list EXPECT success`() {
        val params = mapOf(
            "query" to "favorites",
            "page" to "1",
            "filter" to "id,torrents,playlist,favorite,moon,blockedInfo",
            "rm" to "true"
        )
        val response = PaginatedResponse<ReleaseResponse>(listOf(mockk(), mockk()), mockk())
        val domain = Paginated<Release>(listOf(mockk(), mockk()), mockk())
        val apiResponse = ApiBaseResponse(true, response, null)

        every { service.getList(params) } returns Single.just(apiResponse)
        every { paginationConverter.toDomain(response, any<((ReleaseResponse) -> Release)>()) } returns domain

        dataSource.getList(1).test().assertValue(domain)

        verify { service.getList(params) }
        verify { paginationConverter.toDomain(response, any<((ReleaseResponse) -> Release)>()) }
        confirmVerified(service, releaseConverter, paginationConverter)
    }

    @Test
    fun `add favorite EXPECT success`() {
        val params = mapOf(
            "query" to "favorites",
            "action" to "add",
            "id" to "10"
        )
        val response = mockk<ReleaseResponse>()
        val domain = mockk<Release>()
        val apiResponse = ApiBaseResponse(true, response, null)

        every { service.add(params) } returns Single.just(apiResponse)
        every { releaseConverter.toDomain(response) } returns domain

        dataSource.add(10).test().assertValue(domain)

        verify { service.add(params) }
        verify { releaseConverter.toDomain(response) }
        confirmVerified(service, releaseConverter, paginationConverter)
    }

    @Test
    fun `delete favorite EXPECT success`() {
        val params = mapOf(
            "query" to "favorites",
            "action" to "delete",
            "id" to "10"
        )
        val response = mockk<ReleaseResponse>()
        val domain = mockk<Release>()
        val apiResponse = ApiBaseResponse(true, response, null)

        every { service.delete(params) } returns Single.just(apiResponse)
        every { releaseConverter.toDomain(response) } returns domain

        dataSource.delete(10).test().assertValue(domain)

        verify { service.delete(params) }
        verify { releaseConverter.toDomain(response) }
        confirmVerified(service, releaseConverter, paginationConverter)
    }
}