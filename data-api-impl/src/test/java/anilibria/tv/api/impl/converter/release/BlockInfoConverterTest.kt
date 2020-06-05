package anilibria.tv.api.impl.converter.release

import anilibria.tv.api.common.HtmlUnescapeTool
import anilibria.tv.api.impl.converter.DayConverter
import anilibria.tv.api.impl.converter.ReleaseConverter
import anilibria.tv.api.impl.entity.checker.UpdateLinkResponse
import anilibria.tv.api.impl.entity.checker.UpdateResponse
import anilibria.tv.api.impl.entity.comments.CommentsInfoResponse
import anilibria.tv.api.impl.entity.release.BlockInfoResponse
import anilibria.tv.api.impl.entity.release.FavoriteInfoResponse
import anilibria.tv.domain.entity.checker.Update
import anilibria.tv.domain.entity.checker.UpdateLink
import anilibria.tv.domain.entity.comments.CommentsInfo
import anilibria.tv.domain.entity.release.BlockInfo
import anilibria.tv.domain.entity.release.FavoriteInfo
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class BlockInfoConverterTest {

    private val unescapeTool = mockk<HtmlUnescapeTool>()
    private val dayConverter = DayConverter()
    private val converter = ReleaseConverter(dayConverter, unescapeTool)

    private val response = listOf(
        BlockInfoResponse(
            blocked = true,
            reason = null
        ),
        BlockInfoResponse(
            blocked = false,
            reason = "reason"
        )
    )

    private val domain = listOf(
        BlockInfo(
            blocked = true,
            reason = null
        ),
        BlockInfo(
            blocked = false,
            reason = "reason"
        )
    )

    @Test
    fun `from response EXPECT domain`() {
        val actual = response.map { converter.toDomain(it) }
        assertEquals(domain, actual)
    }
}