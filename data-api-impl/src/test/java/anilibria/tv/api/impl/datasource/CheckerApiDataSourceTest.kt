package anilibria.tv.api.impl.datasource

import anilibria.tv.api.impl.converter.CheckerConverter
import anilibria.tv.api.impl.entity.checker.CheckerResponse
import anilibria.tv.api.impl.entity.checker.UpdateResponse
import anilibria.tv.api.impl.entity.common.ApiBaseResponse
import anilibria.tv.api.impl.service.CheckerService
import anilibria.tv.domain.entity.checker.Update
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.Test

class CheckerApiDataSourceTest {

    private val service = mockk<CheckerService>()
    private val converter = mockk<CheckerConverter>()
    private val dataSource = CheckerApiDataSourceImpl(service, converter)

    private val response = mockk<UpdateResponse>()
    private val domain = mockk<Update>()

    @Test
    fun `get update EXPECT success`() {
        val version = 42
        val params = mapOf(
            "query" to "app_update",
            "current" to "42"
        )
        val apiResponse = ApiBaseResponse(true, CheckerResponse(response), null)

        every { service.get(params) } returns Single.just(apiResponse)
        every { converter.toDomain(response) } returns domain

        dataSource.get(version).test().assertValue(domain)

        verify { service.get(params) }
        verify { converter.toDomain(response) }
        confirmVerified(service, converter)
    }
}