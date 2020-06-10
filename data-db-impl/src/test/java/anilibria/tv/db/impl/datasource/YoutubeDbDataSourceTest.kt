package anilibria.tv.db.impl.datasource

import anilibria.tv.db.impl.converters.YoutubeConverter
import anilibria.tv.db.impl.dao.YoutubeDao
import anilibria.tv.db.impl.entity.youtube.YoutubeDb
import anilibria.tv.domain.entity.common.keys.YoutubeKey
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
        val keys = ids.map { YoutubeKey(it) }
        every { dao.getSome(ids) } returns Single.just(dto)
        every { converter.toDomain(dto) } returns domain

        dataSource.getSome(keys).test().assertValue(domain)

        verify { dao.getSome(ids) }
        verify { converter.toDomain(dto) }
        confirmVerified(dao, converter)
    }

    @Test
    fun `getOne EXPECT success`() {
        val id = 1
        val key = YoutubeKey(id)
        val dtoItem = dto[0]
        val domainItem = domain[0]
        every { dao.getOne(id) } returns Single.just(dtoItem)
        every { converter.toDomain(dtoItem) } returns domainItem

        dataSource.getOne(key).test().assertValue(domainItem)

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
    fun `remove EXPECT success`() {
        val ids = listOf(1, 2)
        val keys = ids.map { YoutubeKey(it) }
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