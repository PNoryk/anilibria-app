package anilibria.tv.cache.impl

import anilibria.tv.cache.impl.memory.EpisodeHistoryMemoryDataSource
import anilibria.tv.domain.entity.common.keys.EpisodeKey
import anilibria.tv.db.EpisodeHistoryDbDataSource
import anilibria.tv.domain.entity.relative.EpisodeHistoryRelative
import io.mockk.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Test

class EpisodeHistoryCacheTest {

    private val dbDataSource = mockk<EpisodeHistoryDbDataSource>()
    private val memoryDataSource = mockk<EpisodeHistoryMemoryDataSource>()
    private val cache = EpisodeHistoryCacheImpl(dbDataSource, memoryDataSource)

    private val mockItems = listOf<EpisodeHistoryRelative>(
        mockk {
            every { releaseId } returns 10
            every { id } returns 1
        },
        mockk {
            every { releaseId } returns 20
            every { id } returns 2
        }
    )

    /* observe */
    @Test
    fun `observe list EXPECT call observe memory`() {
        every { memoryDataSource.observeList() } returns Observable.just(mockItems)

        cache.observeList().test().assertValue(mockItems)

        verify { memoryDataSource.observeList() }
        confirmVerified(dbDataSource, memoryDataSource)
    }

    @Test
    fun `observe some EXPECT call observe memory`() {
        val keys = mockItems.toKeys()
        every { memoryDataSource.observeSome(keys) } returns Observable.just(mockItems)

        cache.observeSome(keys).test().assertValue(mockItems)

        verify { memoryDataSource.observeSome(keys) }
        confirmVerified(dbDataSource, memoryDataSource)
    }

    /* get all list */
    @Test
    fun `get list WHEN db & memory empty EXPECT success empty`() {
        val insertSlot = slot<List<Pair<EpisodeKey, EpisodeHistoryRelative>>>()
        val dbResult = listOf<EpisodeHistoryRelative>()
        val memoryResult = listOf<EpisodeHistoryRelative>()
        val expectResult = emptyList<EpisodeHistoryRelative>()
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
        confirmVerified(dbDataSource, memoryDataSource)
    }

    @Test
    fun `get list WHEN db not empty, memory empty EXPECT success update & get memory from db`() {
        val insertSlot = slot<List<Pair<EpisodeKey, EpisodeHistoryRelative>>>()
        val dbResult = mockItems
        val memoryResult = listOf<EpisodeHistoryRelative>()
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
        confirmVerified(dbDataSource, memoryDataSource)
    }

    @Test
    fun `get list WHEN memory not empty EXPECT success get from memory`() {
        val memoryResult = listOf<EpisodeHistoryRelative>(mockk(), mockk())
        val expectResult = memoryResult.toList()

        every { memoryDataSource.getList() } returns Single.just(memoryResult)

        cache.getList().test().assertValue(expectResult)

        verify { memoryDataSource.getList() }
        confirmVerified(dbDataSource, memoryDataSource)
    }


    /* get by keys */
    @Test
    fun `get by keys WHEN db & memory empty EXPECT success empty`() {
        val insertSlot = slot<List<Pair<EpisodeKey, EpisodeHistoryRelative>>>()
        val dbResult = listOf<EpisodeHistoryRelative>()
        val memoryResult = listOf<EpisodeHistoryRelative>()
        val expectResult = emptyList<EpisodeHistoryRelative>()
        val episodeKeys = mockItems.toKeys()
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
        confirmVerified(dbDataSource, memoryDataSource)
    }

    @Test
    fun `get by keys WHEN db not empty, memory empty EXPECT success update & get memory from db`() {
        val insertSlot = slot<List<Pair<EpisodeKey, EpisodeHistoryRelative>>>()
        val dbResult = mockItems
        val memoryResult = listOf<EpisodeHistoryRelative>()
        val expectResult = dbResult.toList()
        val episodeKeys = mockItems.toKeys()
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
        confirmVerified(dbDataSource, memoryDataSource)
    }

    @Test
    fun `get by keys WHEN memory not empty EXPECT success get from memory`() {
        val memoryResult = mockItems
        val expectResult = memoryResult.toList()
        val episodeKeys = mockItems.toKeys()

        every { memoryDataSource.getSome(episodeKeys) } returns Single.just(memoryResult)

        cache.getSome(episodeKeys).test().assertValue(expectResult)

        verify { memoryDataSource.getSome(episodeKeys) }
        confirmVerified(dbDataSource, memoryDataSource)
    }

    /* put */
    @Test
    fun `put items EXPECT success, put items`() {
        val newItems = mockItems
        val insertKeys = newItems.toKeys()
        val insertKeyValues = newItems.toKeyValues()

        every { dbDataSource.insert(newItems) } returns Completable.complete()
        every { dbDataSource.getSome(insertKeys) } returns Single.just(newItems)
        every { memoryDataSource.insert(insertKeyValues) } returns Completable.complete()

        cache.insert(newItems).test().assertComplete()

        verify { dbDataSource.insert(newItems) }
        verify { dbDataSource.getSome(insertKeys) }
        verify { memoryDataSource.insert(insertKeyValues) }
        confirmVerified(dbDataSource, memoryDataSource)
    }

    /* remove */
    @Test
    fun `remove items EXPECT success, remove items`() {
        val keys = mockItems.toKeys()

        every { dbDataSource.remove(keys) } returns Completable.complete()
        every { memoryDataSource.remove(keys) } returns Completable.complete()

        cache.remove(keys).test().assertComplete()

        verify { dbDataSource.remove(keys) }
        verify { memoryDataSource.remove(keys) }
        confirmVerified(dbDataSource, memoryDataSource)
    }

    @Test
    fun `clear EXPECT success, clear`() {
        every { dbDataSource.clear() } returns Completable.complete()
        every { memoryDataSource.clear() } returns Completable.complete()

        cache.clear().test().assertComplete()

        verify { dbDataSource.clear() }
        verify { memoryDataSource.clear() }
        confirmVerified(dbDataSource, memoryDataSource)
    }

    private fun List<EpisodeHistoryRelative>.toKeys() = map { EpisodeKey(it.releaseId, it.id) }

    private fun List<EpisodeHistoryRelative>.toKeyValues() = map { Pair(EpisodeKey(it.releaseId, it.id), it) }

}