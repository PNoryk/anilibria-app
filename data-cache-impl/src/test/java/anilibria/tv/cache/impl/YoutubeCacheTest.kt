package anilibria.tv.cache.impl

import anilibria.tv.cache.impl.memory.YoutubeMemoryDataSource
import anilibria.tv.cache.impl.merger.YoutubeMerger
import anilibria.tv.db.YoutubeDbDataSource
import anilibria.tv.domain.entity.common.keys.YoutubeKey
import anilibria.tv.domain.entity.youtube.Youtube
import io.mockk.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Test

class YoutubeCacheTest {

    private val dbDataSource = mockk<YoutubeDbDataSource>()
    private val memoryDataSource = mockk<YoutubeMemoryDataSource>()
    private val merger = mockk<YoutubeMerger>()
    private val cache = YoutubeCacheImpl(dbDataSource, memoryDataSource, merger)

    private val mockItems = listOf<Youtube>(
        mockk {
            every { id } returns 10
        },
        mockk {
            every { id } returns 20
        }
    )

    /* observe */
    @Test
    fun `observe list EXPECT call observe memory`() {
        every { memoryDataSource.observeList() } returns Observable.just(mockItems)

        cache.observeList().test().assertValue(mockItems)

        verify { memoryDataSource.observeList() }
        confirmVerified(dbDataSource, memoryDataSource, merger)
    }

    @Test
    fun `observe some EXPECT call observe memory`() {
        val keys = mockItems.toKeys()
        every { memoryDataSource.observeSome(keys) } returns Observable.just(mockItems)

        cache.observeSome(keys).test().assertValue(mockItems)

        verify { memoryDataSource.observeSome(keys) }
        confirmVerified(dbDataSource, memoryDataSource, merger)
    }

    /* get all list */
    @Test
    fun `get list WHEN db & memory empty EXPECT success empty`() {
        val insertSlot = slot<List<Pair<YoutubeKey, Youtube>>>()
        val dbResult = listOf<Youtube>()
        val memoryResult = listOf<Youtube>()
        val expectResult = emptyList<Youtube>()
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
        confirmVerified(dbDataSource, memoryDataSource, merger)
    }

    @Test
    fun `get list WHEN db not empty, memory empty EXPECT success update & get memory from db`() {
        val insertSlot = slot<List<Pair<YoutubeKey, Youtube>>>()
        val dbResult = mockItems
        val memoryResult = listOf<Youtube>()
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
        confirmVerified(dbDataSource, memoryDataSource, merger)
    }

    @Test
    fun `get list WHEN memory not empty EXPECT success get from memory`() {
        val memoryResult = listOf<Youtube>(mockk(), mockk())
        val expectResult = memoryResult.toList()

        every { memoryDataSource.getList() } returns Single.just(memoryResult)

        cache.getList().test().assertValue(expectResult)

        verify { memoryDataSource.getList() }
        confirmVerified(dbDataSource, memoryDataSource, merger)
    }


    /* get by keys */
    @Test
    fun `get by keys WHEN db & memory empty EXPECT success empty`() {
        val insertSlot = slot<List<Pair<YoutubeKey, Youtube>>>()
        val dbResult = listOf<Youtube>()
        val memoryResult = listOf<Youtube>()
        val expectResult = emptyList<Youtube>()
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
        confirmVerified(dbDataSource, memoryDataSource, merger)
    }

    @Test
    fun `get by keys WHEN db not empty, memory empty EXPECT success update & get memory from db`() {
        val insertSlot = slot<List<Pair<YoutubeKey, Youtube>>>()
        val dbResult = mockItems
        val memoryResult = listOf<Youtube>()
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
        confirmVerified(dbDataSource, memoryDataSource, merger)
    }

    @Test
    fun `get by keys WHEN memory not empty EXPECT success get from memory`() {
        val memoryResult = mockItems
        val expectResult = memoryResult.toList()
        val episodeKeys = mockItems.toKeys()

        every { memoryDataSource.getSome(episodeKeys) } returns Single.just(memoryResult)

        cache.getSome(episodeKeys).test().assertValue(expectResult)

        verify { memoryDataSource.getSome(episodeKeys) }
        confirmVerified(dbDataSource, memoryDataSource, merger)
    }

    /* put */
    @Test
    fun `put new items EXPECT success, put new items`() {
        val oldItems = emptyList<Youtube>()
        val newItems = mockItems
        val filteredItems = newItems
        val insertKeys = filteredItems.toKeys()
        val insertKeyValues = filteredItems.toKeyValues()
        val newKeys = newItems.toKeys()

        every { memoryDataSource.getSome(newKeys) } returns Single.just(oldItems)
        every { merger.filterSame(oldItems, newItems) } returns filteredItems
        every { dbDataSource.insert(filteredItems) } returns Completable.complete()
        every { dbDataSource.getSome(insertKeys) } returns Single.just(newItems)
        every { memoryDataSource.insert(insertKeyValues) } returns Completable.complete()

        cache.insert(newItems).test().assertComplete()

        verify { memoryDataSource.getSome(newKeys) }
        verify { merger.filterSame(oldItems, newItems) }
        verify { dbDataSource.insert(filteredItems) }
        verify { dbDataSource.getSome(insertKeys) }
        verify { memoryDataSource.insert(insertKeyValues) }
        confirmVerified(dbDataSource, memoryDataSource, merger)
    }

    @Test
    fun `put exist items EXPECT success, nothing put`() {
        val oldItems = mockItems
        val newItems = mockItems
        val filteredItems = emptyList<Youtube>()
        val newKeys = newItems.toKeys()

        every { memoryDataSource.getSome(newKeys) } returns Single.just(oldItems)
        every { merger.filterSame(oldItems, newItems) } returns filteredItems

        cache.insert(newItems).test().assertComplete()

        verify { memoryDataSource.getSome(newKeys) }
        verify { merger.filterSame(oldItems, newItems) }
        confirmVerified(dbDataSource, memoryDataSource, merger)
    }

    @Test
    fun `put exist & new items EXPECT success, put only new`() {
        val oldItems = mockItems.take(1)
        val newItems = mockItems
        val filteredItems = mockItems.takeLast(1)
        val insertKeys = filteredItems.toKeys()
        val newKeys = newItems.toKeys()
        val newKeyValues = newItems.toKeyValues()

        every { memoryDataSource.getSome(newKeys) } returns Single.just(oldItems)
        every { merger.filterSame(oldItems, newItems) } returns filteredItems
        every { dbDataSource.insert(filteredItems) } returns Completable.complete()
        every { dbDataSource.getSome(insertKeys) } returns Single.just(newItems)
        every { memoryDataSource.insert(newKeyValues) } returns Completable.complete()

        cache.insert(newItems).test().assertComplete()

        verify { memoryDataSource.getSome(newKeys) }
        verify { merger.filterSame(oldItems, newItems) }
        verify { dbDataSource.insert(filteredItems) }
        verify { dbDataSource.getSome(insertKeys) }
        verify { memoryDataSource.insert(newKeyValues) }
        confirmVerified(dbDataSource, memoryDataSource, merger)
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
        confirmVerified(dbDataSource, memoryDataSource, merger)
    }

    @Test
    fun `clear EXPECT success, clear`() {
        every { dbDataSource.clear() } returns Completable.complete()
        every { memoryDataSource.clear() } returns Completable.complete()

        cache.clear().test().assertComplete()

        verify { dbDataSource.clear() }
        verify { memoryDataSource.clear() }
        confirmVerified(dbDataSource, memoryDataSource, merger)
    }

    private fun List<Youtube>.toKeys() = map { YoutubeKey(it.id) }

    private fun List<Youtube>.toKeyValues() = map { Pair(YoutubeKey(it.id), it) }

}