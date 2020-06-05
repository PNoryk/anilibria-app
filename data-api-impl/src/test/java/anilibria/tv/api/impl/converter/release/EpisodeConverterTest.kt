package anilibria.tv.api.impl.converter.release

import anilibria.tv.api.common.HtmlUnescapeTool
import anilibria.tv.api.impl.converter.DayConverter
import anilibria.tv.api.impl.converter.ReleaseConverter
import anilibria.tv.api.impl.entity.checker.UpdateLinkResponse
import anilibria.tv.api.impl.entity.checker.UpdateResponse
import anilibria.tv.api.impl.entity.comments.CommentsInfoResponse
import anilibria.tv.api.impl.entity.release.BlockInfoResponse
import anilibria.tv.api.impl.entity.release.EpisodeResponse
import anilibria.tv.api.impl.entity.release.FavoriteInfoResponse
import anilibria.tv.domain.entity.checker.Update
import anilibria.tv.domain.entity.checker.UpdateLink
import anilibria.tv.domain.entity.comments.CommentsInfo
import anilibria.tv.domain.entity.episode.Episode
import anilibria.tv.domain.entity.release.BlockInfo
import anilibria.tv.domain.entity.release.FavoriteInfo
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class EpisodeConverterTest {

    private val unescapeTool = mockk<HtmlUnescapeTool>()
    private val dayConverter = DayConverter()
    private val converter = ReleaseConverter(dayConverter, unescapeTool)

    private val response = listOf(
        EpisodeResponse(
            id = 1,
            title = "title1",
            sd = "sd1",
            hd = "hd1",
            fullhd = "fullHd1",
            srcSd = "srcSd1",
            srcHd = "srcHd1"
        ),
        EpisodeResponse(
            id = 2,
            title = null,
            sd = null,
            hd = null,
            fullhd = null,
            srcSd = null,
            srcHd = null
        )
    )

    private val domain = listOf(
        Episode(
            releaseId = 10,
            id = 1,
            title = "title1",
            sd = "sd1",
            hd = "hd1",
            fullhd = "fullHd1",
            srcSd = "srcSd1",
            srcHd = "srcHd1"
        ),
        Episode(
            releaseId = 10,
            id = 2,
            title = null,
            sd = null,
            hd = null,
            fullhd = null,
            srcSd = null,
            srcHd = null
        )
    )

    @Test
    fun `from response EXPECT domain`() {
        val actual = response.map { converter.toDomain(10, it) }
        assertEquals(domain, actual)
    }

}