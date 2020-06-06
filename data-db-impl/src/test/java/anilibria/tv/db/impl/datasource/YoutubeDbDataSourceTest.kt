package anilibria.tv.db.impl.datasource

import anilibria.tv.db.impl.converters.YoutubeConverter
import anilibria.tv.db.impl.dao.YoutubeDao
import anilibria.tv.db.impl.entity.youtube.YoutubeDb
import anilibria.tv.domain.entity.youtube.Youtube
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Test

class YoutubeDbDataSourceTest {

    private val dao = mockk<YoutubeDao>()
    private val converter = mockk<YoutubeConverter>()
    private val dataSource = YoutubeDbDataSourceImpl(dao, converter)

    private val dto = listOf<YoutubeDb>(mockk(), mockk())
    private val domain = listOf<Youtube>(mockk(), mockk())

    @Test
    fun `getListAll EXPECT success`() {
        every { dao.getListAll() } returns Single.just(dto)
        every { converter.toDomain(dto) } returns domain

        dataSource.getListAll().test().assertValue(domain)

        verify { dao.getListAll() }
        verify { converter.toDomain(dto) }
        confirmVerified(dao, converter)
    }

    @Test
    fun `getList EXPECT success`() {
        val ids = listOf(1, 2)
        every { dao.getList(ids) } returns Single.just(dto)
        every { converter.toDomain(dto) } returns domain

        dataSource.getList(ids).test().assertValue(domain)

        verify { dao.getList(ids) }
        verify { converter.toDomain(dto) }
        confirmVerified(dao, converter)
    }

    @Test
    fun `getOne EXPECT success`() {
        val id = 1
        val dtoItem = dto[0]
        val domainItem = domain[0]
        every { dao.getOne(id) } returns Single.just(dtoItem)
        every { converter.toDomain(dtoItem) } returns domainItem

        dataSource.getOne(id).test().assertValue(domainItem)

        verify { dao.getOne(id) }
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
        every { dao.delete(ids) } returns Completable.complete()

        dataSource.removeList(ids).test().assertComplete()

        verify { dao.delete(ids) }
        confirmVerified(dao, converter)
    }

    @Test
    fun `deleteAll EXPECT success`() {
        every { dao.deleteAll() } returns Completable.complete()

        dataSource.deleteAll().test().assertComplete()

        verify { dao.deleteAll() }
        confirmVerified(dao, converter)
    }
}