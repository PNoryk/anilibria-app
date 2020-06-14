package anilibria.tv.cache.impl.combiner

import anilibria.tv.cache.EpisodeCache
import anilibria.tv.cache.EpisodeHistoryCache
import anilibria.tv.domain.entity.common.keys.EpisodeKey
import anilibria.tv.domain.entity.converter.EpisodeHistoryRelativeConverter
import anilibria.tv.domain.entity.episode.Episode
import anilibria.tv.domain.entity.history.EpisodeHistory
import anilibria.tv.domain.entity.relative.EpisodeHistoryRelative
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Test

class EpisodeHistoryCacheCombinerTest {

    private val primaryCache = mockk<EpisodeHistoryCache>()
    private val episodeCache = mockk<EpisodeCache>()
    private val relativeConverter = mockk<EpisodeHistoryRelativeConverter>()
    private val combiner = EpisodeHistoryCacheCombinerImpl(primaryCache, episodeCache, relativeConverter)

    private val mockPrimaryItems = listOf<EpisodeHistoryRelative>(
        mockk {
            every { releaseId } returns 10
            every { id } returns 1
        },
        mockk {
            every { releaseId } returns 30
            every { id } returns 3
        }
    )

    private val mockEpisodeItems = listOf<Episode>(
        mockk {
            every { releaseId } returns 10
            every { id } returns 1
        },
        mockk {
            every { releaseId } returns 20
            every { id } returns 2
        },
        mockk {
            every { releaseId } returns 30
            every { id } returns 3
        }
    )

    @Test
    fun `observe list EXPECT observe only exist items`() {
        val expectItems = listOf<EpisodeHistory>(
            mockk {
                every { episode } returns mockEpisodeItems[0]
            }
        )
        val keys = mockPrimaryItems.toKeys()

        every { primaryCache.observeList() } returns Observable.just(mockPrimaryItems)
        every { episodeCache.observeSome(keys) } returns Observable.just(mockEpisodeItems)
        every { relativeConverter.fromRelative(mockPrimaryItems[0], mockEpisodeItems) } returns expectItems[0]
        every { relativeConverter.fromRelative(mockPrimaryItems[1], mockEpisodeItems) } returns null

        combiner.observeList().test().assertValue(expectItems)

        verify { primaryCache.observeList() }
        verify { episodeCache.observeSome(keys) }
        verify { relativeConverter.fromRelative(mockPrimaryItems[0], mockEpisodeItems) }
        verify { relativeConverter.fromRelative(mockPrimaryItems[1], mockEpisodeItems) }
        confirmVerified(primaryCache, episodeCache, relativeConverter)
    }

    @Test
    fun `get list EXPECT get only exist items`() {
        val expectItems = listOf<EpisodeHistory>(
            mockk {
                every { episode } returns mockEpisodeItems[0]
            }
        )
        val keys = mockPrimaryItems.toKeys()

        every { primaryCache.getList() } returns Single.just(mockPrimaryItems)
        every { episodeCache.getSome(keys) } returns Single.just(mockEpisodeItems)
        every { relativeConverter.fromRelative(mockPrimaryItems[0], mockEpisodeItems) } returns expectItems[0]
        every { relativeConverter.fromRelative(mockPrimaryItems[1], mockEpisodeItems) } returns null

        combiner.getList().test().assertValue(expectItems)

        verify { primaryCache.getList() }
        verify { episodeCache.getSome(keys) }
        verify { relativeConverter.fromRelative(mockPrimaryItems[0], mockEpisodeItems) }
        verify { relativeConverter.fromRelative(mockPrimaryItems[1], mockEpisodeItems) }
        confirmVerified(primaryCache, episodeCache, relativeConverter)
    }

    @Test
    fun `insert items EXPECT insert to all caches`() {
        val insertItems = listOf<EpisodeHistory>(
            mockk {
                every { episode } returns mockEpisodeItems[0]
            },
            mockk {
                every { episode } returns mockEpisodeItems[2]
            }
        )
        val insertEpisodes = listOf(mockEpisodeItems[0], mockEpisodeItems[2])
        val insertHistory = mockPrimaryItems

        every { episodeCache.insert(insertEpisodes) } returns Completable.complete()
        every { primaryCache.insert(insertHistory) } returns Completable.complete()
        every { relativeConverter.toRelative(insertItems[0]) } returns mockPrimaryItems[0]
        every { relativeConverter.toRelative(insertItems[1]) } returns mockPrimaryItems[1]

        combiner.insert(insertItems).test().assertComplete()

        verify { episodeCache.insert(insertEpisodes) }
        verify { primaryCache.insert(insertHistory) }
        verify { relativeConverter.toRelative(insertItems[0]) }
        verify { relativeConverter.toRelative(insertItems[1]) }
        confirmVerified(primaryCache, episodeCache, relativeConverter)
    }

    @Test
    fun `remove items EXPECT remove only from primary cache`() {
        val keys = listOf<EpisodeKey>(mockk())
        every { primaryCache.remove(keys) } returns Completable.complete()

        combiner.remove(keys).test().assertComplete()

        verify { primaryCache.remove(keys) }
        confirmVerified(primaryCache, episodeCache, relativeConverter)
    }

    @Test
    fun `clear items EXPECT clear only from primary cache`() {
        every { primaryCache.clear() } returns Completable.complete()

        combiner.clear().test().assertComplete()

        verify { primaryCache.clear() }
        confirmVerified(primaryCache, episodeCache, relativeConverter)
    }

    private fun List<EpisodeHistoryRelative>.toKeys() = map { EpisodeKey(it.releaseId, it.id) }
}