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

class ReleaseStatusConverterTest {

    private val unescapeTool = mockk<HtmlUnescapeTool>()
    private val dayConverter = DayConverter()
    private val converter = ReleaseConverter(dayConverter, unescapeTool)

    private val response = listOf("1", "2", "3", "4")

    private val domain = listOf(
        Release.Status.PROGRESS,
        Release.Status.COMPLETE,
        Release.Status.HIDDEN,
        Release.Status.ONGOING
    )

    @Test
    fun `from response EXPECT domain`() {
        val actual = response.map { converter.parseStatus(it) }
        assertEquals(domain, actual)
    }
}