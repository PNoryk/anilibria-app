package anilibria.tv.api.impl.datasource

import anilibria.tv.api.impl.converter.CheckerConverter
import anilibria.tv.api.impl.converter.CommentsConverter
import anilibria.tv.api.impl.entity.checker.CheckerResponse
import anilibria.tv.api.impl.entity.checker.UpdateResponse
import anilibria.tv.api.impl.entity.comments.CommentsInfoResponse
import anilibria.tv.api.impl.entity.common.ApiBaseResponse
import anilibria.tv.api.impl.service.CheckerService
import anilibria.tv.api.impl.service.CommentsService
import anilibria.tv.domain.entity.checker.Update
import anilibria.tv.domain.entity.comments.CommentsInfo
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.Test

class CommentsApiDataSourceTest {

    private val service = mockk<CommentsService>()
    private val converter = mockk<CommentsConverter>()
    private val dataSource = CommentsApiDataSourceImpl(service, converter)

    private val response = mockk<CommentsInfoResponse>()
    private val domain = mockk<CommentsInfo>()

    @Test
    fun `get EXPECT success`() {
        val params = mapOf("query" to "vkcomments")
        val apiResponse = ApiBaseResponse(true, response, null)

        every { service.getComments(params) } returns Single.just(apiResponse)
        every { converter.toDomain(response) } returns domain

        dataSource.getComments().test().assertValue(domain)

        verify { service.getComments(params) }
        verify { converter.toDomain(response) }
        confirmVerified(service, converter)
    }
}