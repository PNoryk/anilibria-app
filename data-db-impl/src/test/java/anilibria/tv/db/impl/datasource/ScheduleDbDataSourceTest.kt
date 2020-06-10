package anilibria.tv.db.impl.datasource

import anilibria.tv.db.impl.converters.*
import anilibria.tv.db.impl.dao.*
import anilibria.tv.db.impl.entity.schedule.ScheduleDayDb
import anilibria.tv.domain.entity.relative.ScheduleDayRelative
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Test

class ScheduleDbDataSourceTest {

    private val dao = mockk<ScheduleDao>()
    private val converter = mockk<ScheduleConverter>()
    private val dataSource = ScheduleDbDataSourceImpl(dao, converter)

    private val dto = listOf<ScheduleDayDb>(mockk(), mockk())
    private val domain = listOf<ScheduleDayRelative>(mockk(), mockk())

    @Test
    fun `getListAll EXPECT success`() {
        every { dao.getList() } returns Single.just(dto)
        every { converter.toDomain(dto) } returns domain

        dataSource.getList().test().assertValue(domain)

        verify { dao.getList() }
        verify { converter.toDomain(dto) }
        confirmVerified(dao, converter)
    }

    @Test
    fun `getList EXPECT success`() {
        val ids = listOf(1, 2)
        every { dao.getSome(ids) } returns Single.just(dto)
        every { converter.toDomain(dto) } returns domain

        dataSource.getList(ids).test().assertValue(domain)

        verify { dao.getSome(ids) }
        verify { converter.toDomain(dto) }
        confirmVerified(dao, converter)
    }

    @Test
    fun `getOne EXPECT success`() {
        val releaseId = 1
        val dtoItem = dto[0]
        val domainItem = domain[0]
        every { dao.getOne(releaseId) } returns Single.just(dtoItem)
        every { converter.toDomain(dtoItem) } returns domainItem

        dataSource.getOne(releaseId).test().assertValue(domainItem)

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
    fun `removeList EXPECT success`() {
        val ids = listOf(1, 2)
        every { dao.remove(ids) } returns Completable.complete()

        dataSource.remove(ids).test().assertComplete()

        verify { dao.remove(ids) }
        confirmVerified(dao, converter)
    }

    @Test
    fun `deleteAll EXPECT success`() {
        every { dao.clear() } returns Completable.complete()

        dataSource.clear().test().assertComplete()

        verify { dao.clear() }
        confirmVerified(dao, converter)
    }
}