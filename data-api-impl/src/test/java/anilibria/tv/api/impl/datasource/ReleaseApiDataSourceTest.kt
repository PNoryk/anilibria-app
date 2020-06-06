package anilibria.tv.api.impl.datasource

import anilibria.tv.api.impl.converter.FeedConverter
import anilibria.tv.api.impl.converter.PaginationConverter
import anilibria.tv.api.impl.converter.ReleaseConverter
import anilibria.tv.api.impl.entity.common.ApiBaseResponse
import anilibria.tv.api.impl.entity.feed.FeedItemResponse
import anilibria.tv.api.impl.entity.menu.LinkMenuResponse
import anilibria.tv.api.impl.entity.pagination.PaginatedResponse
import anilibria.tv.api.impl.entity.release.RandomReleaseResponse
import anilibria.tv.api.impl.entity.release.ReleaseResponse
import anilibria.tv.api.impl.service.*
import anilibria.tv.domain.entity.feed.Feed
import anilibria.tv.domain.entity.menu.LinkMenu
import anilibria.tv.domain.entity.pagination.Paginated
import anilibria.tv.domain.entity.release.RandomRelease
import anilibria.tv.domain.entity.release.Release
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.Test
import java.lang.IllegalArgumentException

class ReleaseApiDataSourceTest {

    private val service = mockk<ReleaseService>()
    private val releaseConverter = mockk<ReleaseConverter>()
    private val paginationConverter = mockk<PaginationConverter>()
    private val dataSource = ReleaseApiDataSourceImpl(service, releaseConverter, paginationConverter)

    @Test
    fun `get random EXPECT success`() {
        val params = mapOf("query" to "random_release")
        val response = mockk<RandomReleaseResponse>()
        val domain = mockk<RandomRelease>()
        val apiResponse = ApiBaseResponse(true, response, null)

        every { service.getRandom(params) } returns Single.just(apiResponse)
        every { releaseConverter.toDomain(response) } returns domain

        dataSource.getRandom().test().assertValue(domain)

        verify { service.getRandom(params) }
        verify { releaseConverter.toDomain(response) }
        confirmVerified(service, releaseConverter, paginationConverter)
    }

    @Test
    fun `get list EXPECT success`() {
        val params = mapOf(
            "query" to "list",
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
    fun `get one by id EXPECT success`() {
        val params = mapOf(
            "query" to "release",
            "id" to "10"
        )
        val response = mockk<ReleaseResponse>()
        val domain = mockk<Release>()
        val apiResponse = ApiBaseResponse(true, response, null)

        every { service.getOne(params) } returns Single.just(apiResponse)
        every { releaseConverter.toDomain(response) } returns domain

        dataSource.getOne(releaseId = 10).test().assertValue(domain)

        verify { service.getOne(params) }
        verify { releaseConverter.toDomain(response) }
        confirmVerified(service, releaseConverter, paginationConverter)
    }

    @Test
    fun `get one by code EXPECT success`() {
        val params = mapOf(
            "query" to "release",
            "code" to "somecode"
        )
        val response = mockk<ReleaseResponse>()
        val domain = mockk<Release>()
        val apiResponse = ApiBaseResponse(true, response, null)

        every { service.getOne(params) } returns Single.just(apiResponse)
        every { releaseConverter.toDomain(response) } returns domain

        dataSource.getOne(releaseCode = "somecode").test().assertValue(domain)

        verify { service.getOne(params) }
        verify { releaseConverter.toDomain(response) }
        confirmVerified(service, releaseConverter, paginationConverter)
    }

    @Test
    fun `get one by nulls EXPECT error`() {
        val errorClass = IllegalArgumentException::class.java

        dataSource.getOne().test().assertError(errorClass)

        verify(inverse = true) { service.getOne(any()) }
        verify(inverse = true) { releaseConverter.toDomain(any<ReleaseResponse>()) }
        confirmVerified(service, releaseConverter, paginationConverter)
    }

    @Test
    fun `get some by id EXPECT success`() {
        val params = mapOf(
            "query" to "release",
            "id" to "10,20"
        )
        val response = listOf<ReleaseResponse>(mockk(), mockk())
        val domain = listOf<Release>(mockk(), mockk())
        val apiResponse = ApiBaseResponse(true, response, null)

        every { service.getSome(params) } returns Single.just(apiResponse)
        every { releaseConverter.toDomain(response[0]) } returns domain[0]
        every { releaseConverter.toDomain(response[1]) } returns domain[1]

        dataSource.getSome(ids = listOf(10, 20)).test().assertValue(domain)

        verify { service.getSome(params) }
        verify { releaseConverter.toDomain(response[0]) }
        verify { releaseConverter.toDomain(response[1]) }
        confirmVerified(service, releaseConverter, paginationConverter)
    }

    @Test
    fun `get some by code EXPECT success`() {
        val params = mapOf(
            "query" to "release",
            "code" to "code1,code2"
        )
        val response = listOf<ReleaseResponse>(mockk(), mockk())
        val domain = listOf<Release>(mockk(), mockk())
        val apiResponse = ApiBaseResponse(true, response, null)

        every { service.getSome(params) } returns Single.just(apiResponse)
        every { releaseConverter.toDomain(response[0]) } returns domain[0]
        every { releaseConverter.toDomain(response[1]) } returns domain[1]

        dataSource.getSome(codes = listOf("code1", "code2")).test().assertValue(domain)

        verify { service.getSome(params) }
        verify { releaseConverter.toDomain(response[0]) }
        verify { releaseConverter.toDomain(response[1]) }
        confirmVerified(service, releaseConverter, paginationConverter)
    }

    @Test
    fun `get some by nulls EXPECT error`() {
        val errorClass = IllegalArgumentException::class.java

        dataSource.getOne().test().assertError(errorClass)

        verify(inverse = true) { service.getSome(any()) }
        verify(inverse = true) { releaseConverter.toDomain(any<ReleaseResponse>()) }
        confirmVerified(service, releaseConverter, paginationConverter)
    }
}