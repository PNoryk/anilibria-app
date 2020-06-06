package anilibria.tv.api.impl.datasource

import anilibria.tv.api.impl.converter.CheckerConverter
import anilibria.tv.api.impl.converter.CommentsConverter
import anilibria.tv.api.impl.converter.ConfigConverter
import anilibria.tv.api.impl.entity.checker.CheckerResponse
import anilibria.tv.api.impl.entity.checker.UpdateResponse
import anilibria.tv.api.impl.entity.comments.CommentsInfoResponse
import anilibria.tv.api.impl.entity.common.ApiBaseResponse
import anilibria.tv.api.impl.entity.config.ApiAddressResponse
import anilibria.tv.api.impl.entity.config.ConfigResponse
import anilibria.tv.api.impl.service.CheckerService
import anilibria.tv.api.impl.service.CommentsService
import anilibria.tv.api.impl.service.ConfigurationService
import anilibria.tv.domain.entity.checker.Update
import anilibria.tv.domain.entity.comments.CommentsInfo
import anilibria.tv.domain.entity.config.ApiAddress
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.Test

class ConfigurationApiDataSourceTest {

    private val service = mockk<ConfigurationService>()
    private val converter = mockk<ConfigConverter>()
    private val dataSource = ConfigurationApiDataSourceImpl(service, converter)

    private val response = listOf<ApiAddressResponse>(
        mockk(),
        mockk()
    )
    private val domain = listOf<ApiAddress>(
        mockk(),
        mockk()
    )

    @Test
    fun `get config EXPECT success`() {
        val params = mapOf("query" to "config")
        val apiResponse = ApiBaseResponse(true, ConfigResponse(response), null)

        every { service.get(params) } returns Single.just(apiResponse)
        every { converter.toDomain(response[0]) } returns domain[0]
        every { converter.toDomain(response[1]) } returns domain[1]

        dataSource.get().test().assertValue(domain)

        verify { service.get(params) }
        verify { converter.toDomain(response[0]) }
        verify { converter.toDomain(response[1]) }
        confirmVerified(service, converter)
    }
}