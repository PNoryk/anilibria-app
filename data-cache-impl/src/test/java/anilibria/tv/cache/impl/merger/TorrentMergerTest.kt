package anilibria.tv.cache.impl.merger

import anilibria.tv.domain.entity.episode.Episode
import anilibria.tv.domain.entity.torrent.Torrent
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import java.util.*

class TorrentMergerTest {

    private val merger = TorrentMerger()

    private val old = listOf(
        createTorrent(10, 1),
        createTorrent(10, 2),
        createTorrent(20, 2)
    )

    private val new = listOf(
        createTorrent(10, 1, "new"),
        createTorrent(10, 2, "new"),
        createTorrent(20, 2),
        createTorrent(20, 3)
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

    private fun createTorrent(releaseId: Int, id: Int, postfix: String = "") = Torrent(
        releaseId = releaseId,
        id = id,
        hash = "$releaseId$id$postfix",
        leechers = 10,
        seeders = 20,
        completed = 30,
        quality = "quality$releaseId$id$postfix",
        series = "series$releaseId$id$postfix",
        size = 718293L,
        time = Date(12345000L),
        url = "url$releaseId$id$postfix"
    )
}