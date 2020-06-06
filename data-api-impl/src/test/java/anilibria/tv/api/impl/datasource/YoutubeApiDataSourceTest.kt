package anilibria.tv.api.impl.datasource

import anilibria.tv.api.impl.converter.PaginationConverter
import anilibria.tv.api.impl.converter.YoutubeConverter
import anilibria.tv.api.impl.entity.common.ApiBaseResponse
import anilibria.tv.api.impl.entity.pagination.PaginatedResponse
import anilibria.tv.api.impl.entity.youtube.YouTubeResponse
import anilibria.tv.api.impl.service.*
import anilibria.tv.domain.entity.pagination.Paginated
import anilibria.tv.domain.entity.youtube.Youtube
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.Test

class YoutubeApiDataSourceTest {

    private val service = mockk<YoutubeService>()
    private val youtubeConverter = mockk<YoutubeConverter>()
    private val paginationConverter = mockk<PaginationConverter>()
    private val dataSource = YouTubeApiDataSourceImpl(service, youtubeConverter, paginationConverter)

    private val response = PaginatedResponse<YouTubeResponse>(
        listOf(
            mockk(),
            mockk()
        ),
        mockk()
    )

    private val domain = Paginated<Youtube>(
        listOf(
            mockk(),
            mockk()
        ),
        mockk()
    )

    @Test
    fun `get list EXPECT success`() {
        val params = mapOf(
            "query" to "youtube",
            "page" to "1"
        )
        val apiResponse = ApiBaseResponse(true, response, null)

        every { service.getList(params) } returns Single.just(apiResponse)
        every { paginationConverter.toDomain(response, any<((YouTubeResponse) -> Youtube)>()) } returns domain

        dataSource.getList(1).test().assertValue(domain)

        verify { service.getList(params) }
        verify { paginationConverter.toDomain(response, any<((YouTubeResponse) -> Youtube)>()) }
        confirmVerified(service, youtubeConverter, paginationConverter)
    }
}