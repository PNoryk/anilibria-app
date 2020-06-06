package anilibria.tv.cache.impl.merger

import anilibria.tv.domain.entity.release.BlockInfo
import anilibria.tv.domain.entity.release.FavoriteInfo
import anilibria.tv.domain.entity.release.Release
import anilibria.tv.domain.entity.youtube.Youtube
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import java.util.*

class ReleaseMergerTest {

    private val merger = ReleaseMerger()

    private val old = listOf(
        createRelease(10),
        createRelease(20),
        createRelease(30)
    )

    private val new = listOf(
        createRelease(10, "new"),
        createRelease(20, "new"),
        createRelease(30),
        createRelease(40)
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

    private fun createRelease(id: Int, postfix: String = "") = Release(
        id = id,
        code = "code$id$postfix",
        nameRu = "nameRu$id$postfix",
        nameEn = "nameEn$id$postfix",
        series = "series$id$postfix",
        poster = "poster$id$postfix",
        favorite = FavoriteInfo(10, true),
        last = Date(2444000L),
        webPlayer = "webPlayer$id$postfix",
        status = Release.Status.COMPLETE,
        type = "type$id$postfix",
        genres = listOf("genre1", "genre2"),
        voices = listOf("voice1", "voice2"),
        year = "year$id$postfix",
        day = 3,
        description = "description$id$postfix",
        announce = "announce$id$postfix",
        blockedInfo = BlockInfo(false, "reason"),
        playlist = null,
        torrents = null,
        showDonateDialog = true
    )
}