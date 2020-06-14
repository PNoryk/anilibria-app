package anilibria.tv.cache.impl.combiner

import anilibria.tv.cache.FeedCache
import anilibria.tv.cache.YoutubeCache
import anilibria.tv.cache.combiner.ReleaseCacheCombiner
import anilibria.tv.domain.entity.common.keys.FeedKey
import anilibria.tv.domain.entity.common.keys.ReleaseKey
import anilibria.tv.domain.entity.common.keys.YoutubeKey
import anilibria.tv.domain.entity.converter.FeedRelativeConverter
import anilibria.tv.domain.entity.feed.Feed
import anilibria.tv.domain.entity.relative.FeedRelative
import anilibria.tv.domain.entity.release.Release
import anilibria.tv.domain.entity.youtube.Youtube
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Observable
import org.junit.Test

class FeedCacheCombinerTest {

    private val primaryCache = mockk<FeedCache>()
    private val youtubeCache = mockk<YoutubeCache>()
    private val releaseCache = mockk<ReleaseCacheCombiner>()
    private val relativeConverter = mockk<FeedRelativeConverter>()
    private val combiner = FeedCacheCombinerImpl(primaryCache, youtubeCache, releaseCache, relativeConverter)

    private val mockPrimaryItems = listOf<FeedRelative>(
        mockk {
            every { releaseId } returns 10
            every { youtubeId } returns null
        },
        mockk {
            every { releaseId } returns null
            every { youtubeId } returns 123
        },
        mockk {
            every { releaseId } returns null
            every { youtubeId } returns 110
        }
    )

    private val mockReleaseItems = listOf<Release>(
        mockk {
            every { id } returns 10
        }
    )

    private val mockYoutubeItems = listOf<Youtube>(
        mockk {
            every { id } returns 110
        }
    )

    @Test
    fun `observe list EXPECT observe only exist items`() {
        val expectItems = listOf<Feed>(
            mockk {
                every { release } returns mockReleaseItems[0]
                every { youtube } returns null
            },
            mockk {
                every { release } returns null
                every { youtube } returns mockYoutubeItems[0]
            }
        )
        val releaseKeys = mockPrimaryItems.toReleaseKeys()
        val youtubeKeys = mockPrimaryItems.toYoutubeKeys()

        every { primaryCache.observeList() } returns Observable.just(mockPrimaryItems)
        every { releaseCache.observeSome(releaseKeys) } returns Observable.just(mockReleaseItems)
        every { youtubeCache.observeSome(youtubeKeys) } returns Observable.just(mockYoutubeItems)
        every { relativeConverter.fromRelative(mockPrimaryItems[0], mockReleaseItems, mockYoutubeItems) } returns expectItems[0]
        every { relativeConverter.fromRelative(mockPrimaryItems[1], mockReleaseItems, mockYoutubeItems) } returns null
        every { relativeConverter.fromRelative(mockPrimaryItems[2], mockReleaseItems, mockYoutubeItems) } returns expectItems[1]

        combiner.observeList().test().assertValue(expectItems)

        verify { primaryCache.observeList() }
        verify { releaseCache.observeSome(releaseKeys) }
        verify { youtubeCache.observeSome(youtubeKeys) }
        verify { relativeConverter.fromRelative(mockPrimaryItems[0], mockReleaseItems, mockYoutubeItems) }
        verify { relativeConverter.fromRelative(mockPrimaryItems[1], mockReleaseItems, mockYoutubeItems) }
        verify { relativeConverter.fromRelative(mockPrimaryItems[2], mockReleaseItems, mockYoutubeItems) }
        confirmVerified(primaryCache, releaseCache, relativeConverter)
    }

    @Test
    fun `get list EXPECT get only exist items`() {
        val expectItems = listOf<Feed>(
            mockk {
                every { release } returns mockReleaseItems[0]
                every { youtube } returns null
            },
            mockk {
                every { release } returns null
                every { youtube } returns mockYoutubeItems[0]
            }
        )
        val releaseKeys = mockPrimaryItems.toReleaseKeys()
        val youtubeKeys = mockPrimaryItems.toYoutubeKeys()

        every { primaryCache.observeList() } returns Observable.just(mockPrimaryItems)
        every { releaseCache.observeSome(releaseKeys) } returns Observable.just(mockReleaseItems)
        every { youtubeCache.observeSome(youtubeKeys) } returns Observable.just(mockYoutubeItems)
        every { relativeConverter.fromRelative(mockPrimaryItems[0], mockReleaseItems, mockYoutubeItems) } returns expectItems[0]
        every { relativeConverter.fromRelative(mockPrimaryItems[1], mockReleaseItems, mockYoutubeItems) } returns null
        every { relativeConverter.fromRelative(mockPrimaryItems[2], mockReleaseItems, mockYoutubeItems) } returns expectItems[1]

        combiner.observeList().test().assertValue(expectItems)

        verify { primaryCache.observeList() }
        verify { releaseCache.observeSome(releaseKeys) }
        verify { youtubeCache.observeSome(youtubeKeys) }
        verify { relativeConverter.fromRelative(mockPrimaryItems[0], mockReleaseItems, mockYoutubeItems) }
        verify { relativeConverter.fromRelative(mockPrimaryItems[1], mockReleaseItems, mockYoutubeItems) }
        verify { relativeConverter.fromRelative(mockPrimaryItems[2], mockReleaseItems, mockYoutubeItems) }
        confirmVerified(primaryCache, releaseCache, relativeConverter)
    }

    @Test
    fun `insert items EXPECT insert to all caches`() {
        val insertItems = listOf<Feed>(
            mockk {
                every { release } returns mockReleaseItems[0]
                every { youtube } returns null
            },
            mockk {
                every { release } returns null
                every { youtube } returns mockYoutubeItems[0]
            }
        )
        val insertReleases = listOf(mockReleaseItems[0])
        val insertYoutube = listOf(mockYoutubeItems[0])
        val insertFeed = listOf(mockPrimaryItems[0], mockPrimaryItems[2])

        every { releaseCache.insert(insertReleases) } returns Completable.complete()
        every { youtubeCache.insert(insertYoutube) } returns Completable.complete()
        every { primaryCache.insert(insertFeed) } returns Completable.complete()
        every { relativeConverter.toRelative(insertItems[0]) } returns mockPrimaryItems[0]
        every { relativeConverter.toRelative(insertItems[1]) } returns mockPrimaryItems[2]

        combiner.insert(insertItems).test().assertComplete()

        verify { releaseCache.insert(insertReleases) }
        verify { youtubeCache.insert(insertYoutube) }
        verify { primaryCache.insert(insertFeed) }
        verify { relativeConverter.toRelative(insertItems[0]) }
        verify { relativeConverter.toRelative(insertItems[1]) }
        confirmVerified(primaryCache, releaseCache, relativeConverter)
    }

    @Test
    fun `remove items EXPECT remove only from primary cache`() {
        val keys = listOf<FeedKey>(mockk())
        every { primaryCache.remove(keys) } returns Completable.complete()

        combiner.remove(keys).test().assertComplete()

        verify { primaryCache.remove(keys) }
        confirmVerified(primaryCache, releaseCache, relativeConverter)
    }

    @Test
    fun `clear items EXPECT clear only from primary cache`() {
        every { primaryCache.clear() } returns Completable.complete()

        combiner.clear().test().assertComplete()

        verify { primaryCache.clear() }
        confirmVerified(primaryCache, releaseCache, relativeConverter)
    }

    private fun List<FeedRelative>.toReleaseKeys() = mapNotNull { feed -> feed.releaseId?.let { ReleaseKey(it) } }

    private fun List<FeedRelative>.toYoutubeKeys() = mapNotNull { feed -> feed.youtubeId?.let { YoutubeKey(it) } }
}