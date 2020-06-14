package anilibria.tv.cache.impl.combiner

import anilibria.tv.cache.FavoriteCache
import anilibria.tv.cache.combiner.ReleaseCacheCombiner
import anilibria.tv.domain.entity.common.keys.ReleaseKey
import anilibria.tv.domain.entity.converter.FavoriteRelativeConverter
import anilibria.tv.domain.entity.relative.FavoriteRelative
import anilibria.tv.domain.entity.release.Release
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Test

class FavoriteCacheCombinerTest {

    private val primaryCache = mockk<FavoriteCache>()
    private val releaseCache = mockk<ReleaseCacheCombiner>()
    private val relativeConverter = mockk<FavoriteRelativeConverter>()
    private val combiner = FavoriteCacheCombinerImpl(primaryCache, releaseCache, relativeConverter)

    private val mockPrimaryItems = listOf<FavoriteRelative>(
        mockk {
            every { releaseId } returns 10
        },
        mockk {
            every { releaseId } returns 30
        }
    )

    private val mockReleaseItems = listOf<Release>(
        mockk {
            every { id } returns 10
        },
        mockk {
            every { id } returns 20
        },
        mockk {
            every { id } returns 30
        }
    )

    @Test
    fun `observe list EXPECT observe only exist items`() {
        val expectItems = listOf<Release>(
            mockReleaseItems[0]
        )
        val keys = mockPrimaryItems.toKeys()

        every { primaryCache.observeList() } returns Observable.just(mockPrimaryItems)
        every { releaseCache.observeSome(keys) } returns Observable.just(mockReleaseItems)
        every { relativeConverter.fromRelative(mockPrimaryItems[0], mockReleaseItems) } returns expectItems[0]
        every { relativeConverter.fromRelative(mockPrimaryItems[1], mockReleaseItems) } returns null

        combiner.observeList().test().assertValue(expectItems)

        verify { primaryCache.observeList() }
        verify { releaseCache.observeSome(keys) }
        verify { relativeConverter.fromRelative(mockPrimaryItems[0], mockReleaseItems) }
        verify { relativeConverter.fromRelative(mockPrimaryItems[1], mockReleaseItems) }
        confirmVerified(primaryCache, releaseCache, relativeConverter)
    }

    @Test
    fun `get list EXPECT get only exist items`() {
        val expectItems = listOf<Release>(
            mockReleaseItems[0]
        )
        val keys = mockPrimaryItems.toKeys()

        every { primaryCache.getList() } returns Single.just(mockPrimaryItems)
        every { releaseCache.getSome(keys) } returns Single.just(mockReleaseItems)
        every { relativeConverter.fromRelative(mockPrimaryItems[0], mockReleaseItems) } returns expectItems[0]
        every { relativeConverter.fromRelative(mockPrimaryItems[1], mockReleaseItems) } returns null

        combiner.getList().test().assertValue(expectItems)

        verify { primaryCache.getList() }
        verify { releaseCache.getSome(keys) }
        verify { relativeConverter.fromRelative(mockPrimaryItems[0], mockReleaseItems) }
        verify { relativeConverter.fromRelative(mockPrimaryItems[1], mockReleaseItems) }
        confirmVerified(primaryCache, releaseCache, relativeConverter)
    }

    @Test
    fun `insert items EXPECT insert to all caches`() {
        val insertItems = listOf<Release>(
            mockReleaseItems[0],
            mockReleaseItems[2]
        )
        val insertReleases = listOf(mockReleaseItems[0], mockReleaseItems[2])
        val insertFavorites = mockPrimaryItems

        every { releaseCache.insert(insertReleases) } returns Completable.complete()
        every { primaryCache.insert(insertFavorites) } returns Completable.complete()
        every { relativeConverter.toRelative(insertItems[0]) } returns mockPrimaryItems[0]
        every { relativeConverter.toRelative(insertItems[1]) } returns mockPrimaryItems[1]

        combiner.insert(insertItems).test().assertComplete()

        verify { releaseCache.insert(insertReleases) }
        verify { primaryCache.insert(insertFavorites) }
        verify { relativeConverter.toRelative(insertItems[0]) }
        verify { relativeConverter.toRelative(insertItems[1]) }
        confirmVerified(primaryCache, releaseCache, relativeConverter)
    }

    @Test
    fun `remove items EXPECT remove only from primary cache`() {
        val keys = listOf<ReleaseKey>(mockk())
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

    private fun List<FavoriteRelative>.toKeys() = map { ReleaseKey(it.releaseId) }
}