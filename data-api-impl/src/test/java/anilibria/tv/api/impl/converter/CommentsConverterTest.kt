package anilibria.tv.api.impl.converter

import anilibria.tv.api.impl.entity.checker.UpdateLinkResponse
import anilibria.tv.api.impl.entity.checker.UpdateResponse
import anilibria.tv.api.impl.entity.comments.CommentsInfoResponse
import anilibria.tv.domain.entity.checker.Update
import anilibria.tv.domain.entity.checker.UpdateLink
import anilibria.tv.domain.entity.comments.CommentsInfo
import org.junit.Assert.assertEquals
import org.junit.Test

class CommentsConverterTest {

    private val converter = CommentsConverter()

    private val response = CommentsInfoResponse(
        baseUrl = "baseUrl",
        script = "script"
    )

    private val domain = CommentsInfo(
        baseUrl = "baseUrl",
        script = "script"
    )

    @Test
    fun `from response EXPECT domain`() {
        val actual = converter.toDomain(response)
        assertEquals(domain, actual)
    }
}