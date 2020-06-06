package anilibria.tv.api.impl.datasource

import anilibria.tv.api.UserApiDataSource
import anilibria.tv.api.impl.converter.CheckerConverter
import anilibria.tv.api.impl.converter.CommentsConverter
import anilibria.tv.api.impl.converter.UserConverter
import anilibria.tv.api.impl.entity.checker.CheckerResponse
import anilibria.tv.api.impl.entity.checker.UpdateResponse
import anilibria.tv.api.impl.entity.comments.CommentsInfoResponse
import anilibria.tv.api.impl.entity.common.ApiBaseResponse
import anilibria.tv.api.impl.entity.user.UserResponse
import anilibria.tv.api.impl.service.CheckerService
import anilibria.tv.api.impl.service.CommentsService
import anilibria.tv.api.impl.service.UserService
import anilibria.tv.domain.entity.checker.Update
import anilibria.tv.domain.entity.comments.CommentsInfo
import anilibria.tv.domain.entity.user.User
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.Test

class UserApiDataSourceTest {

    private val service = mockk<UserService>()
    private val converter = mockk<UserConverter>()
    private val dataSource = UserApiDataSourceImpl(service, converter)

    private val response = mockk<UserResponse>()
    private val domain = mockk<User>()

    @Test
    fun `get self EXPECT success`() {
        val params = mapOf("query" to "user")
        val apiResponse = ApiBaseResponse(true, response, null)

        every { service.getSelf(params) } returns Single.just(apiResponse)
        every { converter.toDomain(response) } returns domain

        dataSource.getSelf().test().assertValue(domain)

        verify { service.getSelf(params) }
        verify { converter.toDomain(response) }
        confirmVerified(service, converter)
    }
}