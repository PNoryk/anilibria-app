package anilibria.tv.api.impl.converter

import anilibria.tv.api.impl.entity.release.ReleaseResponse
import anilibria.tv.api.impl.entity.schedule.ScheduleDayResponse
import anilibria.tv.domain.entity.release.Release
import anilibria.tv.domain.entity.schedule.ScheduleDay
import io.mockk.*
import org.junit.Assert.assertEquals
import org.junit.Test

class ScheduleConverterTest {

    private val dayConverter = mockk<DayConverter>()
    private val releaseConverter = mockk<ReleaseConverter>()
    private val converter = ScheduleConverter(releaseConverter, dayConverter)

    private val releaseResponse = mockk<ReleaseResponse>()
    private val releaseDomain = mockk<Release>()

    private val response = ScheduleDayResponse(
        "2",
        listOf(releaseResponse)
    )

    private val domain = ScheduleDay(
        3,
        listOf(releaseDomain)
    )

    @Test
    fun `from response EXPECT domain`() {
        every { dayConverter.toDomain(response.day) } returns domain.day
        every { releaseConverter.toDomain(releaseResponse) } returns releaseDomain

        val actual = converter.toDomain(response)

        assertEquals(domain, actual)
        verify { dayConverter.toDomain(response.day) }
        verify { releaseConverter.toDomain(releaseResponse) }
        confirmVerified(releaseConverter)
    }
}