package anilibria.tv.db.impl.datasource

import anilibria.tv.db.impl.converters.FeedConverter
import anilibria.tv.db.impl.dao.FeedDao
import anilibria.tv.db.impl.entity.feed.FeedDb
import anilibria.tv.domain.entity.common.keys.FeedKey
import anilibria.tv.domain.entity.relative.FeedRelative
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Test

class FeedDbDataSourceTest {

    private val dao = mockk<FeedDao>()
    private val converter = mockk<FeedConverter>()
    private val dataSource = FeedDbDataSourceImpl(dao, converter)

    private val dto = listOf<FeedDb>(mockk(), mockk())
    private val domain = listOf<FeedRelative>(mockk(), mockk())

    @Test
    fun `getList EXPECT success`() {
        every { dao.getList() } returns Single.just(dto)
        every { converter.toDomain(dto) } returns domain

        dataSource.getList().test().assertValue(domain)

        verify { dao.getList() }
        verify { converter.toDomain(dto) }
        confirmVerified(dao, converter)
    }


    @Test
    fun `getSome EXPECT success`() {
        val ids = listOf(1 to 10, 2 to 20)
        val keys = listOf(FeedKey(1, 10), FeedKey(2, 20))
        val dbKeys = listOf("1_10", "2_20")
        every { dao.getSome(dbKeys) } returns Single.just(dto)
        every { converter.toDbKey(keys) } returns dbKeys
        every { converter.toDomain(dto) } returns domain

        dataSource.getSome(keys).test().assertValue(domain)

        verify { converter.toDbKey(keys) }
        verify { dao.getSome(dbKeys) }
        verify { converter.toDomain(dto) }
        confirmVerified(dao, converter)
    }

    @Test
    fun `getOne EXPECT success`() {
        val releaseId = 1
        val torrentId = 10
        val key = FeedKey(releaseId, torrentId)
        val dbKey = "1_10"
        val dtoItem = dto[0]
        val domainItem = domain[0]
        every { dao.getOne(dbKey) } returns Single.just(dtoItem)
        every { converter.toDbKey(key) } returns dbKey
        every { converter.toDomain(dtoItem) } returns domainItem

        dataSource.getOne(key).test().assertValue(domainItem)

        verify { dao.getOne(dbKey) }
        verify { converter.toDbKey(key) }
        verify { converter.toDomain(dtoItem) }
        confirmVerified(dao, converter)
    }

    @Test
    fun `insert EXPECT success`() {
        every { converter.toDb(domain) } returns dto
        every { dao.insert(dto) } returns Completable.complete()

        dataSource.insert(domain).test().assertComplete()

        verify { converter.toDb(domain) }
        verify { dao.insert(dto) }
        confirmVerified(dao, converter)
    }

    @Test
    fun `remove EXPECT success`() {
        val keys = listOf(FeedKey(1, 10), FeedKey(2, 20))
        val dbKeys = listOf("1_10", "2_20")
        every { dao.remove(dbKeys) } returns Completable.complete()
        every { converter.toDbKey(keys) } returns dbKeys

        dataSource.remove(keys).test().assertComplete()

        verify { converter.toDbKey(keys) }
        verify { dao.remove(dbKeys) }
        confirmVerified(dao, converter)
    }

    @Test
    fun `clear EXPECT success`() {
        every { dao.clear() } returns Completable.complete()

        dataSource.clear().test().assertComplete()

        verify { dao.clear() }
        confirmVerified(dao, converter)
    }
}