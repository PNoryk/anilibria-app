package anilibria.tv.storage.impl.converter

import anilibria.tv.domain.entity.menu.LinkMenu
import anilibria.tv.storage.impl.entity.LinkMenuStorage
import org.junit.Assert.assertEquals
import org.junit.Test

class LinkMenuConverterTest {

    private val converter = LinkMenuConverter()

    private val IC_TELEGRAM = "telegram"
    private val IC_ANILIBRIA = "anilibria"

    private val listDomain = listOf(
        LinkMenu(
            title = "Title1",
            absoluteLink = "absolute",
            sitePagePath = null,
            icon = IC_TELEGRAM
        ),
        LinkMenu(
            title = "Title2",
            absoluteLink = null,
            sitePagePath = "relative",
            icon = IC_ANILIBRIA
        )
    )

    private val listStorage = listOf(
        LinkMenuStorage(
            title = "Title1",
            absoluteLink = "absolute",
            sitePagePath = null,
            icon = IC_TELEGRAM
        ),
        LinkMenuStorage(
            title = "Title2",
            absoluteLink = null,
            sitePagePath = "relative",
            icon = IC_ANILIBRIA
        )
    )

    @Test
    fun `item from storage EXPECT domain item`() {
        val source = listStorage.first()
        val expected = listDomain.first()
        val actual = converter.toDomain(source)
        assertEquals(expected, actual)
    }

    @Test
    fun `item from domain EXPECT storage item`() {
        val source = listDomain.first()
        val expected = listStorage.first()
        val actual = converter.toStorage(source)
        assertEquals(expected, actual)
    }

    @Test
    fun `items from storage EXPECT domain items`() {
        val source = listStorage
        val expected = listDomain
        val actual = converter.toDomain(source)
        assertEquals(expected, actual)
    }

    @Test
    fun `items from domain EXPECT storage items`() {
        val source = listDomain
        val expected = listStorage
        val actual = converter.toStorage(source)
        assertEquals(expected, actual)
    }
}