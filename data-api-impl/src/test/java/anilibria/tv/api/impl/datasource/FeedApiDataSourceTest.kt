package anilibria.tv.api.impl.datasource

import anilibria.tv.api.impl.converter.FeedConverter
import anilibria.tv.api.impl.converter.PaginationConverter
import anilibria.tv.api.impl.converter.YoutubeConverter
import anilibria.tv.api.impl.entity.common.ApiBaseResponse
import anilibria.tv.api.impl.entity.feed.FeedItemResponse
import anilibria.tv.api.impl.entity.pagination.PaginatedResponse
import anilibria.tv.api.impl.entity.youtube.YouTubeResponse
import anilibria.tv.api.impl.service.*
import anilibria.tv.domain.entity.feed.Feed
import anilibria.tv.domain.entity.pagination.Paginated
import anilibria.tv.domain.entity.youtube.Youtube
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.Test

class FeedApiDataSourceTest {

    private val service = mockk<FeedService>()
    private val feedConverter = mockk<FeedConverter>()
    private val paginationConverter = mockk<PaginationConverter>()
    private val dataSource = FeedApiDataSourceImpl(service, feedConverter, paginationConverter)

    private val response = PaginatedResponse<FeedItemResponse>(
        listOf(
            mockk(),
            mockk()
        ),
        mockk()
    )

    private val domain = Paginated<Feed>(
        listOf(
            mockk(),
            mockk()
        ),
        mockk()
    )

    @Test
    fun `get list EXPECT success`() {
        val params = mapOf(
            "query" to "feed",
            "page" to "1",
            "filter" to "id,torrents,playlist,favorite,moon,blockedInfo",
            "rm" to "true"
        )
        val apiResponse = ApiBaseResponse(true, response, null)

        every { service.getList(params) } returns Single.just(apiResponse)
        every { paginationConverter.toDomain(response, any<((FeedItemResponse) -> Feed)>()) } returns domain

        dataSource.getList(1).test().assertValue(domain)

        verify { service.getList(params) }
        verify { paginationConverter.toDomain(response, any<((FeedItemResponse) -> Feed)>()) }
        confirmVerified(service, feedConverter, paginationConverter)
    }
}