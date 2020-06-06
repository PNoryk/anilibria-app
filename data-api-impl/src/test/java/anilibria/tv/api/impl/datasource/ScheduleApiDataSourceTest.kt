package anilibria.tv.api.impl.datasource

import anilibria.tv.api.impl.converter.*
import anilibria.tv.api.impl.entity.checker.CheckerResponse
import anilibria.tv.api.impl.entity.checker.UpdateResponse
import anilibria.tv.api.impl.entity.comments.CommentsInfoResponse
import anilibria.tv.api.impl.entity.common.ApiBaseResponse
import anilibria.tv.api.impl.entity.config.ApiAddressResponse
import anilibria.tv.api.impl.entity.config.ConfigResponse
import anilibria.tv.api.impl.entity.menu.LinkMenuResponse
import anilibria.tv.api.impl.entity.schedule.ScheduleDayResponse
import anilibria.tv.api.impl.service.*
import anilibria.tv.domain.entity.checker.Update
import anilibria.tv.domain.entity.comments.CommentsInfo
import anilibria.tv.domain.entity.config.ApiAddress
import anilibria.tv.domain.entity.menu.LinkMenu
import anilibria.tv.domain.entity.schedule.ScheduleDay
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.Test

class ScheduleApiDataSourceTest {

    private val service = mockk<ScheduleService>()
    private val converter = mockk<ScheduleConverter>()
    private val dataSource = ScheduleApiDataSourceImpl(service, converter)

    private val response = listOf<ScheduleDayResponse>(
        mockk(),
        mockk()
    )
    private val domain = listOf<ScheduleDay>(
        mockk(),
        mockk()
    )

    @Test
    fun `get list EXPECT success`() {
        val params = mapOf(
            "query" to "schedule",
            "filter" to "id,torrents,playlist,favorite,moon,blockedInfo",
            "rm" to "true"
        )
        val apiResponse = ApiBaseResponse(true, response, null)

        every { service.getList(params) } returns Single.just(apiResponse)
        every { converter.toDomain(response[0]) } returns domain[0]
        every { converter.toDomain(response[1]) } returns domain[1]

        dataSource.getList().test().assertValue(domain)

        verify { service.getList(params) }
        verify { converter.toDomain(response[0]) }
        verify { converter.toDomain(response[1]) }
        confirmVerified(service, converter)
    }
}