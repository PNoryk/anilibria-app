package anilibria.tv.cache.impl

import anilibria.tv.cache.impl.memory.FavoriteMemoryDataSource
import anilibria.tv.cache.impl.memory.ReleaseHistoryMemoryDataSource
import anilibria.tv.db.FavoriteDbDataSource
import anilibria.tv.db.ReleaseHistoryDbDataSource
import anilibria.tv.domain.entity.common.keys.ReleaseKey
import anilibria.tv.domain.entity.relative.FavoriteRelative
import anilibria.tv.domain.entity.relative.ReleaseHistoryRelative
import io.mockk.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Test

class ReleaseHistoryCacheTest {

    private val dbDataSource = mockk<ReleaseHistoryDbDataSource>()
    private val memoryDataSource = mockk<ReleaseHistoryMemoryDataSource>()
    private val cache = ReleaseHistoryCacheImpl(dbDataSource, memoryDataSource)

    private val mockItems = listOf<ReleaseHistoryRelative>(
        mockk {
            every { releaseId } returns 10
        },
        mockk {
            every { releaseId } returns 20
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

    /* get all list */
    @Test
    fun `get list WHEN db & memory empty EXPECT success empty`() {
        val insertSlot = slot<List<Pair<ReleaseKey, ReleaseHistoryRelative>>>()
        val dbResult = listOf<ReleaseHistoryRelative>()
        val memoryResult = listOf<ReleaseHistoryRelative>()
        val expectResult = emptyList<ReleaseHistoryRelative>()
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
        val insertSlot = slot<List<Pair<ReleaseKey, ReleaseHistoryRelative>>>()
        val dbResult = mockItems
        val memoryResult = listOf<ReleaseHistoryRelative>()
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
        val memoryResult = listOf<ReleaseHistoryRelative>(mockk(), mockk())
        val expectResult = memoryResult.toList()

        every { memoryDataSource.getList() } returns Single.just(memoryResult)

        cache.getList().test().assertValue(expectResult)

        verify { memoryDataSource.getList() }
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

    private fun List<ReleaseHistoryRelative>.toKeys() = map { ReleaseKey(it.releaseId) }

    private fun List<ReleaseHistoryRelative>.toKeyValues() = map { Pair(ReleaseKey(it.releaseId), it) }

}