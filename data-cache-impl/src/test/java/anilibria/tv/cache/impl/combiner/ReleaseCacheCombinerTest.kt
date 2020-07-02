package anilibria.tv.cache.impl.combiner

import anilibria.tv.cache.*
import anilibria.tv.domain.entity.common.keys.*
import anilibria.tv.domain.entity.episode.Episode
import anilibria.tv.domain.entity.release.Release
import anilibria.tv.domain.entity.torrent.Torrent
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Test

class ReleaseCacheCombinerTest {

    private val primaryCache = mockk<ReleaseCache>()
    private val episodeCache = mockk<EpisodeCache>()
    private val torrentCache = mockk<TorrentCache>()
    private val combiner = ReleaseCacheCombinerImpl(primaryCache, episodeCache, torrentCache)

    private val mockPrimaryItems = listOf<Release>(
        mockk(relaxed = true) {
            every { id } returns 10
            every { playlist } returns null
            every { torrents } returns null
        },
        mockk(relaxed = true) {
            every { id } returns 11
            every { playlist } returns null
            every { torrents } returns null
        }
    )

    private val mockEpisodeItems = listOf<Episode>(
        mockk {
            every { releaseId } returns 10
            every { id } returns 20
        },
        mockk {
            every { releaseId } returns 11
            every { id } returns 21
        }
    )

    private val mockTorrentItems = listOf<Torrent>(
        mockk {
            every { releaseId } returns 10
            every { id } returns 30
        },
        mockk {
            every { releaseId } returns 11
            every { id } returns 31
        }
    )

    /* OBSERVE */
    @Test
    fun `observe list EXPECT observe only exist items`() {
        val expectedItems = listOf(
            mockPrimaryItems[0].copy(
                playlist = listOf(mockEpisodeItems[0]),
                torrents = listOf(mockTorrentItems[0])
            ),
            mockPrimaryItems[1].copy(
                playlist = listOf(mockEpisodeItems[1]),
                torrents = listOf(mockTorrentItems[1])
            )
        )
        val cachedItems = mockPrimaryItems
        val episodeKeys = cachedItems.toEpisodeKeys()
        val torrentKeys = cachedItems.toTorrentKeys()

        every { primaryCache.observeList() } returns Observable.just(cachedItems)
        every { episodeCache.observeSome(episodeKeys) } returns Observable.just(mockEpisodeItems)
        every { torrentCache.observeSome(torrentKeys) } returns Observable.just(mockTorrentItems)

        combiner.observeList().test().assertValue(expectedItems)

        verify { primaryCache.observeList() }
        verify { episodeCache.observeSome(episodeKeys) }
        verify { torrentCache.observeSome(torrentKeys) }
        confirmVerified(primaryCache, episodeCache, torrentCache)
    }

    @Test
    fun `observe some EXPECT observe only exist items`() {
        val expectedItems = listOf(
            mockPrimaryItems[1].copy(
                playlist = listOf(mockEpisodeItems[1]),
                torrents = listOf(mockTorrentItems[1])
            )
        )
        val cachedItems = listOf(mockPrimaryItems[1])
        val releaseKeys = listOf(ReleaseKey(11))
        val episodeKeys = cachedItems.toEpisodeKeys()
        val torrentKeys = cachedItems.toTorrentKeys()

        every { primaryCache.observeSome(releaseKeys) } returns Observable.just(cachedItems)
        every { episodeCache.observeSome(episodeKeys) } returns Observable.just(mockEpisodeItems)
        every { torrentCache.observeSome(torrentKeys) } returns Observable.just(mockTorrentItems)

        combiner.observeSome(releaseKeys).test().assertValue(expectedItems)

        verify { primaryCache.observeSome(releaseKeys) }
        verify { episodeCache.observeSome(episodeKeys) }
        verify { torrentCache.observeSome(torrentKeys) }
        confirmVerified(primaryCache, episodeCache, torrentCache)
    }

    @Test
    fun `observe one EXPECT observe only exist items`() {
        val expectedItems = mockPrimaryItems[1].copy(
            playlist = listOf(mockEpisodeItems[1]),
            torrents = listOf(mockTorrentItems[1])
        )
        val cachedItems = mockPrimaryItems[1]
        val releaseKey = ReleaseKey(11)
        val episodeKeys = listOf(cachedItems).toEpisodeKeys()
        val torrentKeys = listOf(cachedItems).toTorrentKeys()

        every { primaryCache.observeOne(releaseKey) } returns Observable.just(cachedItems)
        every { episodeCache.observeSome(episodeKeys) } returns Observable.just(mockEpisodeItems)
        every { torrentCache.observeSome(torrentKeys) } returns Observable.just(mockTorrentItems)

        combiner.observeOne(releaseKey).test().assertValue(expectedItems)

        verify { primaryCache.observeOne(releaseKey) }
        verify { episodeCache.observeSome(episodeKeys) }
        verify { torrentCache.observeSome(torrentKeys) }
        confirmVerified(primaryCache, episodeCache, torrentCache)
    }

    /* GET */
    @Test
    fun `get list EXPECT get only exist items`() {
        val expectedItems = listOf(
            mockPrimaryItems[0].copy(
                playlist = listOf(mockEpisodeItems[0]),
                torrents = listOf(mockTorrentItems[0])
            ),
            mockPrimaryItems[1].copy(
                playlist = listOf(mockEpisodeItems[1]),
                torrents = listOf(mockTorrentItems[1])
            )
        )
        val cachedItems = mockPrimaryItems
        val episodeKeys = cachedItems.toEpisodeKeys()
        val torrentKeys = cachedItems.toTorrentKeys()

        every { primaryCache.getList() } returns Single.just(cachedItems)
        every { episodeCache.getSome(episodeKeys) } returns Single.just(mockEpisodeItems)
        every { torrentCache.getSome(torrentKeys) } returns Single.just(mockTorrentItems)

        combiner.getList().test().assertValue(expectedItems)

        verify { primaryCache.getList() }
        verify { episodeCache.getSome(episodeKeys) }
        verify { torrentCache.getSome(torrentKeys) }
        confirmVerified(primaryCache, episodeCache, torrentCache)
    }

    @Test
    fun `get some EXPECT get only exist items`() {
        val expectedItems = listOf(
            mockPrimaryItems[1].copy(
                playlist = listOf(mockEpisodeItems[1]),
                torrents = listOf(mockTorrentItems[1])
            )
        )
        val cachedItems = listOf(mockPrimaryItems[1])
        val releaseKeys = listOf(ReleaseKey(11))
        val episodeKeys = cachedItems.toEpisodeKeys()
        val torrentKeys = cachedItems.toTorrentKeys()

        every { primaryCache.getSome(releaseKeys) } returns Single.just(cachedItems)
        every { episodeCache.getSome(episodeKeys) } returns Single.just(mockEpisodeItems)
        every { torrentCache.getSome(torrentKeys) } returns Single.just(mockTorrentItems)

        combiner.getSome(releaseKeys).test().assertValue(expectedItems)

        verify { primaryCache.getSome(releaseKeys) }
        verify { episodeCache.getSome(episodeKeys) }
        verify { torrentCache.getSome(torrentKeys) }
        confirmVerified(primaryCache, episodeCache, torrentCache)
    }

    @Test
    fun `get one EXPECT get only exist items`() {
        val expectedItems = mockPrimaryItems[1].copy(
            playlist = listOf(mockEpisodeItems[1]),
            torrents = listOf(mockTorrentItems[1])
        )
        val cachedItems = mockPrimaryItems[1]
        val releaseKey = ReleaseKey(11)
        val episodeKeys = listOf(cachedItems).toEpisodeKeys()
        val torrentKeys = listOf(cachedItems).toTorrentKeys()

        every { primaryCache.getOne(releaseKey) } returns Single.just(cachedItems)
        every { episodeCache.getSome(episodeKeys) } returns Single.just(mockEpisodeItems)
        every { torrentCache.getSome(torrentKeys) } returns Single.just(mockTorrentItems)

        combiner.getOne(releaseKey).test().assertValue(expectedItems)

        verify { primaryCache.getOne(releaseKey) }
        verify { episodeCache.getSome(episodeKeys) }
        verify { torrentCache.getSome(torrentKeys) }
        confirmVerified(primaryCache, episodeCache, torrentCache)
    }

    /* OTHER */
    @Test
    fun `insert items EXPECT insert to all caches`() {
        val insertItems = listOf<Release>(
            mockk(relaxed = true) {
                every { id } returns 10
                every { playlist } returns null
                every { torrents } returns listOf(mockTorrentItems[0])
            },
            mockk(relaxed = true) {
                every { id } returns 11
                every { playlist } returns listOf(mockEpisodeItems[1])
                every { torrents } returns null
            }
        )

        val insertEpisodes = listOf(mockEpisodeItems[1])
        val insertTorrents = listOf(mockTorrentItems[0])
        val insertReleases = listOf(
            insertItems[0].copy(playlist = null, torrents = null),
            insertItems[1].copy(playlist = null, torrents = null)
        )

        every { episodeCache.insert(insertEpisodes) } returns Completable.complete()
        every { torrentCache.insert(insertTorrents) } returns Completable.complete()
        every { primaryCache.insert(insertReleases) } returns Completable.complete()

        combiner.insert(insertItems).test().assertComplete()

        verify { episodeCache.insert(insertEpisodes) }
        verify { torrentCache.insert(insertTorrents) }
        verify { primaryCache.insert(insertReleases) }
        confirmVerified(primaryCache, episodeCache, torrentCache)
    }

    @Test
    fun `remove items EXPECT remove only from primary cache`() {
        val keys = listOf<ReleaseKey>(mockk())
        every { primaryCache.remove(keys) } returns Completable.complete()

        combiner.remove(keys).test().assertComplete()

        verify { primaryCache.remove(keys) }
        confirmVerified(primaryCache, episodeCache, torrentCache)
    }

    @Test
    fun `clear items EXPECT clear only from primary cache`() {
        every { primaryCache.clear() } returns Completable.complete()

        combiner.clear().test().assertComplete()

        verify { primaryCache.clear() }
        confirmVerified(primaryCache, episodeCache, torrentCache)
    }

    private fun Release.toEpisodeKey() = EpisodeKey(id, null)
    private fun List<Release>.toEpisodeKeys() = map { it.toEpisodeKey() }

    private fun Release.toTorrentKey() = TorrentKey(id, null)
    private fun List<Release>.toTorrentKeys() = map { it.toTorrentKey() }
}