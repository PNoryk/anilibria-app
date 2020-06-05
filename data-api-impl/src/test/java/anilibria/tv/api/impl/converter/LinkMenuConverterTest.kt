package anilibria.tv.api.impl.converter

import anilibria.tv.api.impl.entity.checker.UpdateLinkResponse
import anilibria.tv.api.impl.entity.checker.UpdateResponse
import anilibria.tv.api.impl.entity.comments.CommentsInfoResponse
import anilibria.tv.api.impl.entity.menu.LinkMenuResponse
import anilibria.tv.domain.entity.checker.Update
import anilibria.tv.domain.entity.checker.UpdateLink
import anilibria.tv.domain.entity.comments.CommentsInfo
import anilibria.tv.domain.entity.menu.LinkMenu
import org.junit.Assert.assertEquals
import org.junit.Test

class LinkMenuConverterTest {

    private val converter = LinkMenuConverter()

    private val response = listOf(
        LinkMenuResponse(
            title = "Title1",
            absoluteLink = "absolute",
            sitePagePath = null,
            icon = "telegram"
        ),
        LinkMenuResponse(
            title = "Title2",
            absoluteLink = null,
            sitePagePath = "relative",
            icon = "anilibria"
        )
    )

    private val domain = listOf(
        LinkMenu(
            title = "Title1",
            absoluteLink = "absolute",
            sitePagePath = null,
            icon = "telegram"
        ),
        LinkMenu(
            title = "Title2",
            absoluteLink = null,
            sitePagePath = "relative",
            icon = "anilibria"
        )
    )

    @Test
    fun `from response EXPECT domain`() {
        val actual = response.map { converter.toDomain(it) }
        assertEquals(domain, actual)
    }
}