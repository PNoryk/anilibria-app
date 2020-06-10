package anilibria.tv.db.impl.datasource

import anilibria.tv.db.impl.converters.ReleaseHistoryConverter
import anilibria.tv.db.impl.dao.ReleaseHistoryDao
import anilibria.tv.db.impl.entity.history.ReleaseHistoryDb
import anilibria.tv.domain.entity.common.keys.ReleaseKey
import anilibria.tv.domain.entity.relative.ReleaseHistoryRelative
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Test

class ReleaseHistoryDbDataSourceTest {

    private val dao = mockk<ReleaseHistoryDao>()
    private val converter = mockk<ReleaseHistoryConverter>()
    private val dataSource = ReleaseHistoryDbDataSourceImpl(dao, converter)

    private val dto = listOf<ReleaseHistoryDb>(mockk(), mockk())
    private val domain = listOf<ReleaseHistoryRelative>(mockk(), mockk())

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
        val ids = listOf(1, 2)
        val keys = ids.map { ReleaseKey(it) }
        every { dao.getSome(ids) } returns Single.just(dto)
        every { converter.toDomain(dto) } returns domain

        dataSource.getSome(keys).test().assertValue(domain)

        verify { dao.getSome(ids) }
        verify { converter.toDomain(dto) }
        confirmVerified(dao, converter)
    }

    @Test
    fun `getOne EXPECT success`() {
        val releaseId = 1
        val key = ReleaseKey(releaseId)
        val dtoItem = dto[0]
        val domainItem = domain[0]
        every { dao.getOne(releaseId) } returns Single.just(dtoItem)
        every { converter.toDomain(dtoItem) } returns domainItem

        dataSource.getOne(key).test().assertValue(domainItem)

        verify { dao.getOne(releaseId) }
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
        val ids = listOf(1, 2)
        val keys = ids.map { ReleaseKey(it) }
        every { dao.remove(ids) } returns Completable.complete()

        dataSource.remove(keys).test().assertComplete()

        verify { dao.remove(ids) }
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