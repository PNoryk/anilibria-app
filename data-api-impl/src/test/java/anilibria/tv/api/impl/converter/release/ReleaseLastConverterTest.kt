package anilibria.tv.api.impl.converter.release

import anilibria.tv.api.common.HtmlUnescapeTool
import anilibria.tv.api.impl.converter.DayConverter
import anilibria.tv.api.impl.converter.ReleaseConverter
import anilibria.tv.api.impl.entity.checker.UpdateLinkResponse
import anilibria.tv.api.impl.entity.checker.UpdateResponse
import anilibria.tv.api.impl.entity.comments.CommentsInfoResponse
import anilibria.tv.api.impl.entity.release.FavoriteInfoResponse
import anilibria.tv.domain.entity.checker.Update
import anilibria.tv.domain.entity.checker.UpdateLink
import anilibria.tv.domain.entity.comments.CommentsInfo
import anilibria.tv.domain.entity.release.FavoriteInfo
import anilibria.tv.domain.entity.release.Release
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class ReleaseLastConverterTest {

    private val unescapeTool = mockk<HtmlUnescapeTool>()
    private val dayConverter = DayConverter()
    private val converter = ReleaseConverter(dayConverter, unescapeTool)

    @Test
    fun `from response EXPECT domain`() {
        val response = "12345"
        val domain = Date(12345000L)
        val actual = converter.parseLast(response)
        assertEquals(domain, actual)
    }

    @Test
    fun `from wrong response EXPECT domain`() {
        val response = "hello"
        val domain: Date? = null
        val actual = converter.parseLast(response)
        assertEquals(domain, actual)
    }
}