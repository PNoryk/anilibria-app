package ru.radiationx.data

import org.junit.Assert.*
import org.junit.Test
import anilibria.tv.cache.merger.EpisodeMerger
import anilibria.tv.domain.entity.release.Episode

class SimpleTest {

    @Test
    fun checkEpisodeMerge() {
        val merger = EpisodeMerger()

        val oldEpisode = createEpisode(10, 1).copy(title = "old")
        val newEpisode = createEpisode(10, 1).copy(title = "new")

        val expected = createEpisode(10, 1).copy(title = "new")

        val merged = merger.merge(oldEpisode, newEpisode)

        assertEquals(expected, merged)
    }

    @Test
    fun checkEpisodeListMerge() {
        val merger = EpisodeMerger()

        val oldReleaseRange = (1..3)
        val oldEpisodeRange = (1..2)

        val newReleaseRange = (1..1)
        val newEpisodeRange = (1..1)

        val oldItems = oldReleaseRange.map { releaseId ->
            oldEpisodeRange.map { episodeId ->
                createEpisode(releaseId, episodeId).copy(title = "old")
            }
        }.flatten()

        val newItems = newReleaseRange.map { releaseId ->
            newEpisodeRange.map { episodeId ->
                createEpisode(releaseId, episodeId).copy(title = "new")
            }
        }.flatten()

        val expected = newReleaseRange.map { releaseId ->
            newEpisodeRange.map { episodeId ->
                createEpisode(releaseId, episodeId).copy(title = "new")
            }
        }.flatten()

        val filtered = merger.filter(oldItems, newItems)

        assertEquals(expected, filtered)
    }

    private fun createEpisode(releaseId: Int, id: Int) = Episode(
        releaseId = releaseId,
        id = id,
        title = null,
        sd = null,
        hd = null,
        fullhd = null,
        srcSd = null,
        srcHd = null
    )

}