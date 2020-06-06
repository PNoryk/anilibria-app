package anilibria.tv.cache.impl.merger

import anilibria.tv.domain.entity.episode.Episode
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class EpisodeMergerTest {

    private val merger = EpisodeMerger()

    private val old = listOf(
        createEpisode(10, 1),
        createEpisode(10, 2),
        createEpisode(20, 2)
    )

    private val new = listOf(
        createEpisode(10, 1, "new"),
        createEpisode(10, 2, "new"),
        createEpisode(20, 2),
        createEpisode(20, 3)
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

    private fun createEpisode(releaseId: Int, id: Int, postfix: String = "") = Episode(
        releaseId = releaseId,
        id = id,
        title = "title$releaseId$id$postfix",
        sd = "sd$releaseId$id$postfix",
        hd = "hd$releaseId$id$postfix",
        fullhd = "fullHd$releaseId$id$postfix",
        srcSd = "srcSd$releaseId$id$postfix",
        srcHd = "srcHd$releaseId$id$postfix"
    )
}