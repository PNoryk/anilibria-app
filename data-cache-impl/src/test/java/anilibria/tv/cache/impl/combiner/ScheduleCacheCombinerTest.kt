package anilibria.tv.cache.impl.combiner

import anilibria.tv.cache.ScheduleCache
import anilibria.tv.cache.combiner.ReleaseCacheCombiner
import anilibria.tv.domain.entity.common.keys.ReleaseKey
import anilibria.tv.domain.entity.common.keys.ScheduleKey
import anilibria.tv.domain.entity.converter.ScheduleDayRelativeConverter
import anilibria.tv.domain.entity.relative.ScheduleDayRelative
import anilibria.tv.domain.entity.release.Release
import anilibria.tv.domain.entity.schedule.ScheduleDay
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Test

class ScheduleCacheCombinerTest {

    private val primaryCache = mockk<ScheduleCache>()
    private val releaseCache = mockk<ReleaseCacheCombiner>()
    private val relativeConverter = mockk<ScheduleDayRelativeConverter>()
    private val combiner = ScheduleCacheCombinerImpl(primaryCache, releaseCache, relativeConverter)

    private val mockPrimaryItems = listOf<ScheduleDayRelative>(
        mockk {
            every { dayId } returns 1
            every { releaseIds } returns listOf(10)
        },
        mockk {
            every { dayId } returns 3
            every { releaseIds } returns listOf(30)
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
        val expectItems = listOf<ScheduleDay>(
            mockk {
                every { day } returns 1
                every { items } returns listOf(mockReleaseItems[0])
            },
            mockk {
                every { day } returns 3
                every { items } returns listOf(mockReleaseItems[2])
            }
        )
        val keys = mockPrimaryItems.toReleaseKeys()

        every { primaryCache.observeList() } returns Observable.just(mockPrimaryItems)
        every { releaseCache.observeSome(keys) } returns Observable.just(mockReleaseItems)
        every { relativeConverter.fromRelative(mockPrimaryItems[0], mockReleaseItems) } returns expectItems[0]
        every { relativeConverter.fromRelative(mockPrimaryItems[1], mockReleaseItems) } returns expectItems[1]

        combiner.observeList().test().assertValue(expectItems)

        verify { primaryCache.observeList() }
        verify { releaseCache.observeSome(keys) }
        verify { relativeConverter.fromRelative(mockPrimaryItems[0], mockReleaseItems) }
        verify { relativeConverter.fromRelative(mockPrimaryItems[1], mockReleaseItems) }
        confirmVerified(primaryCache, releaseCache, relativeConverter)
    }

    @Test
    fun `get list EXPECT get only exist items`() {
        val expectItems = listOf<ScheduleDay>(
            mockk {
                every { day } returns 1
                every { items } returns listOf(mockReleaseItems[0])
            },
            mockk {
                every { day } returns 3
                every { items } returns listOf(mockReleaseItems[2])
            }
        )
        val keys = mockPrimaryItems.toReleaseKeys()

        every { primaryCache.getList() } returns Single.just(mockPrimaryItems)
        every { releaseCache.getSome(keys) } returns Single.just(mockReleaseItems)
        every { relativeConverter.fromRelative(mockPrimaryItems[0], mockReleaseItems) } returns expectItems[0]
        every { relativeConverter.fromRelative(mockPrimaryItems[1], mockReleaseItems) } returns expectItems[1]

        combiner.getList().test().assertValue(expectItems)

        verify { primaryCache.getList() }
        verify { releaseCache.getSome(keys) }
        verify { relativeConverter.fromRelative(mockPrimaryItems[0], mockReleaseItems) }
        verify { relativeConverter.fromRelative(mockPrimaryItems[1], mockReleaseItems) }
        confirmVerified(primaryCache, releaseCache, relativeConverter)
    }

    @Test
    fun `insert items EXPECT insert to all caches`() {
        val insertItems = listOf<ScheduleDay>(
            mockk {
                every { day } returns 1
                every { items } returns listOf(mockReleaseItems[0])
            },
            mockk {
                every { day } returns 3
                every { items } returns listOf(mockReleaseItems[2])
            }
        )
        val insertReleases = listOf(mockReleaseItems[0], mockReleaseItems[2])
        val insertSchedule = mockPrimaryItems

        every { releaseCache.insert(insertReleases) } returns Completable.complete()
        every { primaryCache.insert(insertSchedule) } returns Completable.complete()
        every { relativeConverter.toRelative(insertItems[0]) } returns mockPrimaryItems[0]
        every { relativeConverter.toRelative(insertItems[1]) } returns mockPrimaryItems[1]

        combiner.insert(insertItems).test().assertComplete()

        verify { releaseCache.insert(insertReleases) }
        verify { primaryCache.insert(insertSchedule) }
        verify { relativeConverter.toRelative(insertItems[0]) }
        verify { relativeConverter.toRelative(insertItems[1]) }
        confirmVerified(primaryCache, releaseCache, relativeConverter)
    }

    @Test
    fun `remove items EXPECT remove only from primary cache`() {
        val keys = listOf<ScheduleKey>(mockk())
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

    private fun List<ScheduleDayRelative>.toReleaseKeys() = map { it.releaseIds }.flatten().map { ReleaseKey(it) }
}