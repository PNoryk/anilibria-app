package anilibria.tv.cache.impl

import anilibria.tv.cache.impl.memory.EpisodeMemoryDataSource
import anilibria.tv.domain.entity.common.keys.EpisodeKey
import anilibria.tv.cache.impl.merger.EpisodeMerger
import anilibria.tv.db.EpisodeDbDataSource
import anilibria.tv.domain.entity.episode.Episode
import io.mockk.*
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Test

class EpisodeCacheTest {

    private val dbDataSource = mockk<EpisodeDbDataSource>()
    private val memoryDataSource = mockk<EpisodeMemoryDataSource>()
    private val episodeMerger = mockk<EpisodeMerger>()
    private val cache = EpisodeCacheImpl(dbDataSource, memoryDataSource, episodeMerger)

    private val mockEpisodes = listOf<Episode>(
        mockk {
            every { releaseId } returns 10
            every { id } returns 1
        },
        mockk {
            every { releaseId } returns 20
            every { id } returns 2
        }
    )

    /* get all list */
    @Test
    fun `get list WHEN db & memory empty EXPECT success empty`() {
        val insertSlot = slot<List<Pair<EpisodeKey, Episode>>>()
        val dbResult = listOf<Episode>()
        val memoryResult = listOf<Episode>()
        val expectResult = emptyList<Episode>()
        val insertKeyValues = dbResult.toKeyValues()

        every { memoryDataSource.getList() } returns Single.fromCallable {
            if (insertSlot.isCaptured) dbResult else memoryResult
        }
        every { dbDataSource.getList() } returns Single.just(dbResult)
        every { memoryDataSource.insert(capture(insertSlot)) } returns Completable.complete()

        cache.getList().test().assertValue(expectResult)

        verify { memoryDataSource.getList() }
        verify { dbDataSource.getList() }
        verify { memoryDataSource.insert(insertKeyValues) }
        confirmVerified(dbDataSource, memoryDataSource, episodeMerger)
    }

    @Test
    fun `get list WHEN db not empty, memory empty EXPECT success update & get memory from db`() {
        val insertSlot = slot<List<Pair<EpisodeKey, Episode>>>()
        val dbResult = mockEpisodes
        val memoryResult = listOf<Episode>()
        val expectResult = dbResult.toList()
        val insertKeyValues = dbResult.toKeyValues()

        every { memoryDataSource.getList() } returns Single.fromCallable {
            if (insertSlot.isCaptured) dbResult else memoryResult
        }
        every { dbDataSource.getList() } returns Single.just(dbResult)
        every { memoryDataSource.insert(capture(insertSlot)) } returns Completable.complete()

        cache.getList().test().assertValue(expectResult)

        verify { memoryDataSource.getList() }
        verify { dbDataSource.getList() }
        verify { memoryDataSource.insert(insertKeyValues) }
        confirmVerified(dbDataSource, memoryDataSource, episodeMerger)
    }

    @Test
    fun `get list WHEN memory not empty EXPECT success get from memory`() {
        val memoryResult = listOf<Episode>(mockk(), mockk())
        val expectResult = memoryResult.toList()

        every { memoryDataSource.getList() } returns Single.just(memoryResult)

        cache.getList().test().assertValue(expectResult)

        verify { memoryDataSource.getList() }
        confirmVerified(dbDataSource, memoryDataSource, episodeMerger)
    }


    /* get by keys */
    @Test
    fun `get by keys WHEN db & memory empty EXPECT success empty`() {
        val insertSlot = slot<List<Pair<EpisodeKey, Episode>>>()
        val dbResult = listOf<Episode>()
        val memoryResult = listOf<Episode>()
        val expectResult = emptyList<Episode>()
        val episodeKeys = mockEpisodes.toKeys()
        val insertKeyValues = dbResult.toKeyValues()

        every { memoryDataSource.getSome(episodeKeys) } returns Single.fromCallable {
            if (insertSlot.isCaptured) dbResult else memoryResult
        }
        every { dbDataSource.getSome(episodeKeys) } returns Single.just(dbResult)
        every { memoryDataSource.insert(capture(insertSlot)) } returns Completable.complete()

        cache.getSome(episodeKeys).test().assertValue(expectResult)

        verify { memoryDataSource.getSome(episodeKeys) }
        verify { dbDataSource.getSome(episodeKeys) }
        verify { memoryDataSource.insert(insertKeyValues) }
        confirmVerified(dbDataSource, memoryDataSource, episodeMerger)
    }

    @Test
    fun `get by keys WHEN db not empty, memory empty EXPECT success update & get memory from db`() {
        val insertSlot = slot<List<Pair<EpisodeKey, Episode>>>()
        val dbResult = mockEpisodes
        val memoryResult = listOf<Episode>()
        val expectResult = dbResult.toList()
        val episodeKeys = mockEpisodes.toKeys()
        val insertKeyValues = dbResult.toKeyValues()

        every { memoryDataSource.getSome(episodeKeys) } returns Single.fromCallable {
            if (insertSlot.isCaptured) dbResult else memoryResult
        }
        every { dbDataSource.getSome(episodeKeys) } returns Single.just(dbResult)
        every { memoryDataSource.insert(capture(insertSlot)) } returns Completable.complete()

        cache.getSome(episodeKeys).test().assertValue(expectResult)

        verify { memoryDataSource.getSome(episodeKeys) }
        verify { dbDataSource.getSome(episodeKeys) }
        verify { memoryDataSource.insert(insertKeyValues) }
        confirmVerified(dbDataSource, memoryDataSource, episodeMerger)
    }

    @Test
    fun `get by keys WHEN memory not empty EXPECT success get from memory`() {
        val memoryResult = mockEpisodes
        val expectResult = memoryResult.toList()
        val episodeKeys = mockEpisodes.toKeys()

        every { memoryDataSource.getSome(episodeKeys) } returns Single.just(memoryResult)

        cache.getSome(episodeKeys).test().assertValue(expectResult)

        verify { memoryDataSource.getSome(episodeKeys) }
        confirmVerified(dbDataSource, memoryDataSource, episodeMerger)
    }

    /* put */
    @Test
    fun `put new items EXPECT success, put new items`() {
        val oldItems = emptyList<Episode>()
        val newItems = mockEpisodes
        val filteredItems = newItems
        val insertKeys = filteredItems.toKeys()
        val insertKeyValues = filteredItems.toKeyValues()
        val newKeys = newItems.toKeys()

        every { memoryDataSource.getSome(newKeys) } returns Single.just(oldItems)
        every { episodeMerger.filterSame(oldItems, newItems) } returns filteredItems
        every { dbDataSource.insert(filteredItems) } returns Completable.complete()
        every { dbDataSource.getSome(insertKeys) } returns Single.just(newItems)
        every { memoryDataSource.insert(insertKeyValues) } returns Completable.complete()

        cache.insert(newItems).test().assertComplete()

        verify { memoryDataSource.getSome(newKeys) }
        verify { episodeMerger.filterSame(oldItems, newItems) }
        verify { dbDataSource.insert(filteredItems) }
        verify { dbDataSource.getSome(insertKeys) }
        verify { memoryDataSource.insert(insertKeyValues) }
        confirmVerified(dbDataSource, memoryDataSource, episodeMerger)
    }

    @Test
    fun `put exist items EXPECT success, nothing put`() {
        val oldItems = mockEpisodes
        val newItems = mockEpisodes
        val filteredItems = emptyList<Episode>()
        val newKeys = newItems.toKeys()

        every { memoryDataSource.getSome(newKeys) } returns Single.just(oldItems)
        every { episodeMerger.filterSame(oldItems, newItems) } returns filteredItems

        cache.insert(newItems).test().assertComplete()

        verify { memoryDataSource.getSome(newKeys) }
        verify { episodeMerger.filterSame(oldItems, newItems) }
        confirmVerified(dbDataSource, memoryDataSource, episodeMerger)
    }

    @Test
    fun `put exist & new items EXPECT success, put only new`() {
        val oldItems = mockEpisodes.take(1)
        val newItems = mockEpisodes
        val filteredItems = mockEpisodes.takeLast(1)
        val insertKeys = filteredItems.toKeys()
        val newKeys = newItems.toKeys()
        val newKeyValues = newItems.toKeyValues()

        every { memoryDataSource.getSome(newKeys) } returns Single.just(oldItems)
        every { episodeMerger.filterSame(oldItems, newItems) } returns filteredItems
        every { dbDataSource.insert(filteredItems) } returns Completable.complete()
        every { dbDataSource.getSome(insertKeys) } returns Single.just(newItems)
        every { memoryDataSource.insert(newKeyValues) } returns Completable.complete()

        cache.insert(newItems).test().assertComplete()

        verify { memoryDataSource.getSome(newKeys) }
        verify { episodeMerger.filterSame(oldItems, newItems) }
        verify { dbDataSource.insert(filteredItems) }
        verify { dbDataSource.getSome(insertKeys) }
        verify { memoryDataSource.insert(newKeyValues) }
        confirmVerified(dbDataSource, memoryDataSource, episodeMerger)
    }

    /* remove */
    @Test
    fun `remove items EXPECT success, remove items`() {
        val keys = mockEpisodes.toKeys()

        every { dbDataSource.remove(keys) } returns Completable.complete()
        every { memoryDataSource.remove(keys) } returns Completable.complete()

        cache.remove(keys).test().assertComplete()

        verify { dbDataSource.remove(keys) }
        verify { memoryDataSource.remove(keys) }
        confirmVerified(dbDataSource, memoryDataSource, episodeMerger)
    }

    @Test
    fun `clear EXPECT success, clear`() {
        every { dbDataSource.clear() } returns Completable.complete()
        every { memoryDataSource.clear() } returns Completable.complete()

        cache.clear().test().assertComplete()

        verify { dbDataSource.clear() }
        verify { memoryDataSource.clear() }
        confirmVerified(dbDataSource, memoryDataSource, episodeMerger)
    }

    private fun List<Episode>.toKeys() = map { EpisodeKey(it.releaseId, it.id) }

    private fun List<Episode>.toKeyValues() = map { Pair(EpisodeKey(it.releaseId, it.id), it) }

}