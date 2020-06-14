package anilibria.tv.cache.impl

import anilibria.tv.cache.impl.memory.TorrentMemoryDataSource
import anilibria.tv.cache.impl.merger.TorrentMerger
import anilibria.tv.db.TorrentDbDataSource
import anilibria.tv.domain.entity.common.keys.TorrentKey
import anilibria.tv.domain.entity.torrent.Torrent
import io.mockk.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Test

class TorrentCacheTest {

    private val dbDataSource = mockk<TorrentDbDataSource>()
    private val memoryDataSource = mockk<TorrentMemoryDataSource>()
    private val merger = mockk<TorrentMerger>()
    private val cache = TorrentCacheImpl(dbDataSource, memoryDataSource, merger)

    private val mockItems = listOf<Torrent>(
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
        val insertSlot = slot<List<Pair<TorrentKey, Torrent>>>()
        val dbResult = listOf<Torrent>()
        val memoryResult = listOf<Torrent>()
        val expectResult = emptyList<Torrent>()
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
        val insertSlot = slot<List<Pair<TorrentKey, Torrent>>>()
        val dbResult = mockItems
        val memoryResult = listOf<Torrent>()
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
        val memoryResult = listOf<Torrent>(mockk(), mockk())
        val expectResult = memoryResult.toList()

        every { memoryDataSource.getList() } returns Single.just(memoryResult)

        cache.getList().test().assertValue(expectResult)

        verify { memoryDataSource.getList() }
        confirmVerified(dbDataSource, memoryDataSource, merger)
    }


    /* get by keys */
    @Test
    fun `get by keys WHEN db & memory empty EXPECT success empty`() {
        val insertSlot = slot<List<Pair<TorrentKey, Torrent>>>()
        val dbResult = listOf<Torrent>()
        val memoryResult = listOf<Torrent>()
        val expectResult = emptyList<Torrent>()
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
        val insertSlot = slot<List<Pair<TorrentKey, Torrent>>>()
        val dbResult = mockItems
        val memoryResult = listOf<Torrent>()
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
        val oldItems = emptyList<Torrent>()
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
        val filteredItems = emptyList<Torrent>()
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

    private fun List<Torrent>.toKeys() = map { TorrentKey(it.releaseId, it.id) }

    private fun List<Torrent>.toKeyValues() = map { Pair(TorrentKey(it.releaseId, it.id), it) }

}