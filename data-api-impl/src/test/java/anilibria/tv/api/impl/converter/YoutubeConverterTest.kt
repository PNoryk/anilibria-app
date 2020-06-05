package anilibria.tv.api.impl.converter

import anilibria.tv.api.impl.converter.common.DummyHtmlUnescapeTool
import anilibria.tv.api.impl.entity.checker.UpdateLinkResponse
import anilibria.tv.api.impl.entity.checker.UpdateResponse
import anilibria.tv.api.impl.entity.comments.CommentsInfoResponse
import anilibria.tv.api.impl.entity.user.UserResponse
import anilibria.tv.api.impl.entity.youtube.YouTubeResponse
import anilibria.tv.domain.entity.checker.Update
import anilibria.tv.domain.entity.checker.UpdateLink
import anilibria.tv.domain.entity.comments.CommentsInfo
import anilibria.tv.domain.entity.user.User
import anilibria.tv.domain.entity.youtube.Youtube
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class YoutubeConverterTest {

    private val unescapeTool = DummyHtmlUnescapeTool()
    private val converter = YoutubeConverter(unescapeTool)

    private val response = listOf(
        YouTubeResponse(
            id = 10,
            title = "title",
            image = "image",
            vid = "vid",
            views = 1000,
            comments = 2000,
            timestamp = 5432L
        ),
        YouTubeResponse(
            id = 10,
            title = null,
            image = null,
            vid = null,
            views = 0,
            comments = 0,
            timestamp = 0L
        )
    )

    private val domain = listOf(
        Youtube(
            id = 10,
            title = "title",
            image = "image",
            vid = "vid",
            views = 1000,
            comments = 2000,
            timestamp = Date(5432000L)
        ),
        Youtube(
            id = 10,
            title = null,
            image = null,
            vid = null,
            views = 0,
            comments = 0,
            timestamp = Date(0L)
        )
    )

    @Test
    fun `from response EXPECT domain`() {
        val actual = response.map { converter.toDomain(it) }
        assertEquals(domain, actual)
    }
}