package anilibria.tv.cache.impl

import anilibria.tv.cache.impl.memory.FavoriteMemoryDataSource
import anilibria.tv.cache.impl.memory.FeedMemoryDataSource
import anilibria.tv.cache.impl.memory.GenreMemoryDataSource
import anilibria.tv.cache.impl.memory.LinkMenuMemoryDataSource
import anilibria.tv.db.FavoriteDbDataSource
import anilibria.tv.db.FeedDbDataSource
import anilibria.tv.domain.entity.common.keys.FeedKey
import anilibria.tv.domain.entity.common.keys.ReleaseKey
import anilibria.tv.domain.entity.episode.Episode
import anilibria.tv.domain.entity.menu.LinkMenu
import anilibria.tv.domain.entity.relative.FavoriteRelative
import anilibria.tv.domain.entity.relative.FeedRelative
import anilibria.tv.storage.GenreStorageDataSource
import anilibria.tv.storage.LinkMenuStorageDataSource
import io.mockk.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Test

class LinkMenuCacheTest {

    private val storageDataSource = mockk<LinkMenuStorageDataSource>()
    private val memoryDataSource = mockk<LinkMenuMemoryDataSource>()
    private val cache = LinkMenuCacheImpl(storageDataSource, memoryDataSource)

    private val mockItems = listOf<LinkMenu>(
        mockk(),
        mockk()
    )

    /* observe */
    @Test
    fun `observe list EXPECT call observe memory`() {
        every { memoryDataSource.observeList() } returns Observable.just(mockItems)

        cache.observeList().test().assertValue(mockItems)

        verify { memoryDataSource.observeList() }
        confirmVerified(storageDataSource, memoryDataSource)
    }

    /* get all list */
    @Test
    fun `get list WHEN db & memory empty EXPECT success empty`() {
        val insertSlot = slot<List<LinkMenu>>()
        val dbResult = listOf<LinkMenu>()
        val memoryResult = listOf<LinkMenu>()
        val expectResult = emptyList<LinkMenu>()

        every { memoryDataSource.getList() } returns Single.fromCallable {
            if (insertSlot.isCaptured) dbResult else memoryResult
        }
        every { storageDataSource.getList() } returns Single.just(dbResult)
        every { memoryDataSource.insert(capture(insertSlot)) } returns Completable.complete()

        cache.getList().test().assertValue(expectResult)

        verify { memoryDataSource.getList() }
        verify { storageDataSource.getList() }
        verify { memoryDataSource.insert(capture(insertSlot)) }
        confirmVerified(storageDataSource, memoryDataSource)
    }

    @Test
    fun `get list WHEN db not empty, memory empty EXPECT success update & get memory from db`() {
        val insertSlot = slot<List<LinkMenu>>()
        val dbResult = mockItems
        val memoryResult = listOf<LinkMenu>()
        val expectResult = dbResult.toList()

        every { memoryDataSource.getList() } returns Single.fromCallable {
            if (insertSlot.isCaptured) dbResult else memoryResult
        }
        every { storageDataSource.getList() } returns Single.just(dbResult)
        every { memoryDataSource.insert(capture(insertSlot)) } returns Completable.complete()

        cache.getList().test().assertValue(expectResult)

        verify { memoryDataSource.getList() }
        verify { storageDataSource.getList() }
        verify { memoryDataSource.insert(capture(insertSlot)) }
        confirmVerified(storageDataSource, memoryDataSource)
    }

    @Test
    fun `get list WHEN memory not empty EXPECT success get from memory`() {
        val memoryResult = listOf<LinkMenu>(mockk(), mockk())
        val expectResult = memoryResult.toList()

        every { memoryDataSource.getList() } returns Single.just(memoryResult)

        cache.getList().test().assertValue(expectResult)

        verify { memoryDataSource.getList() }
        confirmVerified(storageDataSource, memoryDataSource)
    }

    /* put */
    @Test
    fun `put items EXPECT success, put items`() {
        val newItems = mockItems

        every { storageDataSource.putList(newItems) } returns Completable.complete()
        every { storageDataSource.getList() } returns Single.just(newItems)
        every { memoryDataSource.insert(newItems) } returns Completable.complete()

        cache.putList(newItems).test().assertComplete()

        verify { storageDataSource.putList(newItems) }
        verify { storageDataSource.getList() }
        verify { memoryDataSource.insert(newItems) }
        confirmVerified(storageDataSource, memoryDataSource)
    }

    /* remove */
    @Test
    fun `clear EXPECT success, clear`() {
        every { storageDataSource.clear() } returns Completable.complete()
        every { memoryDataSource.clear() } returns Completable.complete()

        cache.clear().test().assertComplete()

        verify { storageDataSource.clear() }
        verify { memoryDataSource.clear() }
        confirmVerified(storageDataSource, memoryDataSource)
    }
}