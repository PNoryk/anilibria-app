package anilibria.tv.cache.impl

import anilibria.tv.cache.impl.memory.EpisodeMemoryDataSource
import anilibria.tv.cache.impl.memory.keys.EpisodeMemoryKey
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
        val insertSlot = slot<List<Pair<EpisodeMemoryKey, Episode>>>()
        val dbResult = listOf<Episode>()
        val memoryResult = listOf<Episode>()
        val expectResult = emptyList<Episode>()
        val insertKeyValues = dbResult.toKeyValues()

        every { memoryDataSource.getList() } returns Single.fromCallable {
            if (insertSlot.isCaptured) dbResult else memoryResult
        }
        every { dbDataSource.getListAll() } returns Single.just(dbResult)
        every { memoryDataSource.insert(capture(insertSlot)) } returns Completable.complete()

        cache.getList().test().assertValue(expectResult)

        verify { memoryDataSource.getList() }
        verify { dbDataSource.getListAll() }
        verify { memoryDataSource.insert(insertKeyValues) }
        confirmVerified(dbDataSource, memoryDataSource, episodeMerger)
    }

    @Test
    fun `get list WHEN db not empty, memory empty EXPECT success update & get memory from db`() {
        val insertSlot = slot<List<Pair<EpisodeMemoryKey, Episode>>>()
        val dbResult = mockEpisodes
        val memoryResult = listOf<Episode>()
        val expectResult = dbResult.toList()
        val insertKeyValues = dbResult.toKeyValues()

        every { memoryDataSource.getList() } returns Single.fromCallable {
            if (insertSlot.isCaptured) dbResult else memoryResult
        }
        every { dbDataSource.getListAll() } returns Single.just(dbResult)
        every { memoryDataSource.insert(capture(insertSlot)) } returns Completable.complete()

        cache.getList().test().assertValue(expectResult)

        verify { memoryDataSource.getList() }
        verify { dbDataSource.getListAll() }
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
        val insertSlot = slot<List<Pair<EpisodeMemoryKey, Episode>>>()
        val dbResult = listOf<Episode>()
        val memoryResult = listOf<Episode>()
        val expectResult = emptyList<Episode>()
        val releaseIds = listOf(10, 20)
        val releaseKeys = releaseIds.toReleaseKeys()
        val insertKeyValues = dbResult.toKeyValues()

        every { memoryDataSource.getSome(releaseKeys) } returns Single.fromCallable {
            if (insertSlot.isCaptured) dbResult else memoryResult
        }
        every { dbDataSource.getList(releaseIds) } returns Single.just(dbResult)
        every { memoryDataSource.insert(capture(insertSlot)) } returns Completable.complete()

        cache.getList(releaseIds).test().assertValue(expectResult)

        verify { memoryDataSource.getSome(releaseKeys) }
        verify { dbDataSource.getList(releaseIds) }
        verify { memoryDataSource.insert(insertKeyValues) }
        confirmVerified(dbDataSource, memoryDataSource, episodeMerger)
    }

    @Test
    fun `get by keys WHEN db not empty, memory empty EXPECT success update & get memory from db`() {
        val insertSlot = slot<List<Pair<EpisodeMemoryKey, Episode>>>()
        val dbResult = mockEpisodes
        val memoryResult = listOf<Episode>()
        val expectResult = dbResult.toList()
        val releaseIds = listOf(10, 20)
        val releaseKeys = releaseIds.toReleaseKeys()
        val insertKeyValues = dbResult.toKeyValues()

        every { memoryDataSource.getSome(releaseKeys) } returns Single.fromCallable {
            if (insertSlot.isCaptured) dbResult else memoryResult
        }
        every { dbDataSource.getList(releaseIds) } returns Single.just(dbResult)
        every { memoryDataSource.insert(capture(insertSlot)) } returns Completable.complete()

        cache.getList(releaseIds).test().assertValue(expectResult)

        verify { memoryDataSource.getSome(releaseKeys) }
        verify { dbDataSource.getList(releaseIds) }
        verify { memoryDataSource.insert(insertKeyValues) }
        confirmVerified(dbDataSource, memoryDataSource, episodeMerger)
    }

    @Test
    fun `get by keys WHEN memory not empty EXPECT success get from memory`() {
        val memoryResult = mockEpisodes
        val expectResult = memoryResult.toList()
        val releaseIds = listOf(10, 20)
        val releaseKeys = releaseIds.toReleaseKeys()

        every { memoryDataSource.getSome(releaseKeys) } returns Single.just(memoryResult)

        cache.getList(releaseIds).test().assertValue(expectResult)

        verify { memoryDataSource.getSome(releaseKeys) }
        confirmVerified(dbDataSource, memoryDataSource, episodeMerger)
    }

    /* put */
    @Test
    fun `put new items EXPECT success, put new items`() {
        val oldItems = emptyList<Episode>()
        val newItems = mockEpisodes
        val newKeys = newItems.toKeys()
        val newDbIds = newItems.toIds()
        val newKeyValues = newItems.toKeyValues()

        every { memoryDataSource.getSome(newKeys) } returns Single.just(oldItems)
        every { episodeMerger.filterSame(oldItems, newItems) } returns newItems
        every { dbDataSource.insert(newItems) } returns Completable.complete()
        every { dbDataSource.getListByPairIds(newDbIds) } returns Single.just(newItems)
        every { memoryDataSource.insert(newKeyValues) } returns Completable.complete()

        cache.putList(mockEpisodes).test().assertComplete()


        confirmVerified(dbDataSource, memoryDataSource, episodeMerger)
    }

    @Test
    fun `put exist items EXPECT success, nothing put`() {
    }

    @Test
    fun `put exist & new items EXPECT success, put only new`() {
    }

    /* remove */
    @Test
    fun `remove items EXPECT success, remove items`() {
    }

    @Test
    fun `clear EXPECT success, clear`() {
    }


    private fun List<Episode>.toIds() = map { Pair(it.releaseId, it.id) }

    private fun List<Int>.toReleaseKeys() = map { EpisodeMemoryKey(it, null) }

    private fun List<Episode>.toKeys() = map { EpisodeMemoryKey(it.releaseId, it.id) }

    private fun List<Episode>.toKeyValues() = map { Pair(EpisodeMemoryKey(it.releaseId, it.id), it) }

}