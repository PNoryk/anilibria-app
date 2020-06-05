package anilibria.tv.api.impl.converter

import anilibria.tv.api.impl.entity.auth.OtpInfoResponse
import anilibria.tv.api.impl.entity.auth.SocialServiceResponse
import anilibria.tv.domain.entity.auth.OtpInfo
import anilibria.tv.domain.entity.auth.SocialService
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class AuthConverterTest {

    private val authConverter = AuthConverter()

    @Test
    fun `from OtpInfo response EXPECT domain`() {
        val response = OtpInfoResponse(
            code = "code",
            description = "description",
            expiresAt = 1591382183L,
            remainingTime = 50L
        )
        val domain = OtpInfo(
            code = "code",
            description = "description",
            expiresAt = Date(1591382183000L),
            remainingTime = 50L
        )
        val actual = authConverter.toDomain(response)
        assertEquals(domain, actual)
    }

    @Test
    fun `from SocialService response EXPECT domain`() {
        val response = SocialServiceResponse(
            key = "key",
            title = "title",
            socialUrl = "socialUrl",
            resultPattern = "resultPattern",
            errorUrlPattern = "errorUrlPattern"
        )
        val domain = SocialService(
            key = "key",
            title = "title",
            socialUrl = "socialUrl",
            resultPattern = "resultPattern",
            errorUrlPattern = "errorUrlPattern"
        )
        val actual = authConverter.toDomain(response)
        assertEquals(domain, actual)
    }
}