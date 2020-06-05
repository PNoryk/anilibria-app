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
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class FavoriteInfoConverterTest {

    private val unescapeTool = mockk<HtmlUnescapeTool>()
    private val dayConverter = DayConverter()
    private val converter = ReleaseConverter(dayConverter, unescapeTool)

    private val response = FavoriteInfoResponse(
        rating = 10,
        added = true
    )

    private val domain = FavoriteInfo(
        rating = 10,
        added = true
    )

    @Test
    fun `from response EXPECT domain`() {
        val actual = converter.toDomain(response)
        assertEquals(domain, actual)
    }
}