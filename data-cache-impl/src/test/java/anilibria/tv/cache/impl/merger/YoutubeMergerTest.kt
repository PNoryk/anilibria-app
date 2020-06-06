package anilibria.tv.cache.impl.merger

import anilibria.tv.domain.entity.torrent.Torrent
import anilibria.tv.domain.entity.youtube.Youtube
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import java.util.*

class YoutubeMergerTest {

    private val merger = YoutubeMerger()

    private val old = listOf(
        createYoutube(10),
        createYoutube(20),
        createYoutube(30)
    )

    private val new = listOf(
        createYoutube(10, "new"),
        createYoutube(20, "new"),
        createYoutube(30),
        createYoutube(40)
    )

    @Test
    fun `merge with different keys EXPECT error`() {
        assertThrows(IllegalArgumentException::class.java) {
            merger.merge(old[0], new[1])
        }
    }

    @Test
    fun `merge with same keys EXPECT success`() {
        val actual = merger.merge(old[0], new[0])
        assertEquals(new[0], actual)
    }

    @Test
    fun `filter same items EXPECT success`() {
        val expect = listOf(new[0], new[1], new[3])
        val actual = merger.filterSame(old, new)
        assertEquals(expect, actual)
    }

    private fun createYoutube(id: Int, postfix: String = "") = Youtube(
        id = id,
        title = "$id$postfix",
        image = "$id$postfix",
        vid = "$id$postfix",
        views = 0,
        comments = 0,
        timestamp = Date(0)
    )
}