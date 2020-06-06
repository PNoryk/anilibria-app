package anilibria.tv.db.impl.datasource

import anilibria.tv.db.impl.converters.TorrentConverter
import anilibria.tv.db.impl.dao.TorrentDao
import anilibria.tv.db.impl.entity.torrent.TorrentDb
import anilibria.tv.domain.entity.torrent.Torrent
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Test

class TorrentDbDataSourceTest {

    private val dao = mockk<TorrentDao>()
    private val converter = mockk<TorrentConverter>()
    private val dataSource = TorrentDbDataSourceImpl(dao, converter)

    private val dto = listOf<TorrentDb>(mockk(), mockk())
    private val domain = listOf<Torrent>(mockk(), mockk())

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
    fun `getListByPairIds EXPECT success`() {
        val ids = listOf(1 to 10, 2 to 20)
        val keys = listOf("1_10", "2_20")
        every { dao.getListByKeys(keys) } returns Single.just(dto)
        every { converter.toDbKey(ids) } returns keys
        every { converter.toDomain(dto) } returns domain

        dataSource.getListByPairIds(ids).test().assertValue(domain)

        verify { converter.toDbKey(ids) }
        verify { dao.getListByKeys(keys) }
        verify { converter.toDomain(dto) }
        confirmVerified(dao, converter)
    }

    @Test
    fun `getOne EXPECT success`() {
        val releaseId = 1
        val torrentId = 10
        val dtoItem = dto[0]
        val domainItem = domain[0]
        every { dao.getOne(releaseId, torrentId) } returns Single.just(dtoItem)
        every { converter.toDomain(dtoItem) } returns domainItem

        dataSource.getOne(releaseId, torrentId).test().assertValue(domainItem)

        verify { dao.getOne(releaseId, torrentId) }
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
        val ids = listOf(1 to 10, 2 to 20)
        val keys = listOf("1_10", "2_20")
        every { dao.delete(keys) } returns Completable.complete()
        every { converter.toDbKey(ids) } returns keys

        dataSource.removeList(ids).test().assertComplete()

        verify { converter.toDbKey(ids) }
        verify { dao.delete(keys) }
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