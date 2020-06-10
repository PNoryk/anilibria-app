package anilibria.tv.db.impl.datasource

import anilibria.tv.db.impl.converters.*
import anilibria.tv.db.impl.dao.*
import anilibria.tv.db.impl.entity.favorite.FavoriteDb
import anilibria.tv.domain.entity.relative.FavoriteRelative
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Test

class FavoriteDbDataSourceTest {

    private val dao = mockk<FavoriteDao>()
    private val converter = mockk<FavoriteConverter>()
    private val dataSource = FavoriteDbDataSourceImpl(dao, converter)

    private val dto = listOf<FavoriteDb>(mockk(), mockk())
    private val domain = listOf<FavoriteRelative>(mockk(), mockk())

    @Test
    fun `getListAll EXPECT success`() {
        every { dao.getSome() } returns Single.just(dto)
        every { converter.toDomain(dto) } returns domain

        dataSource.getList().test().assertValue(domain)

        verify { dao.getSome() }
        verify { converter.toDomain(dto) }
        confirmVerified(dao, converter)
    }

    @Test
    fun `getList EXPECT success`() {
        val ids = listOf(1, 2)
        every { dao.getSome(ids) } returns Single.just(dto)
        every { converter.toDomain(dto) } returns domain

        dataSource.getSome(ids).test().assertValue(domain)

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