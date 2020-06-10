package anilibria.tv.db.impl.datasource

import anilibria.tv.db.impl.converters.EpisodeConverter
import anilibria.tv.db.impl.dao.EpisodeDao
import anilibria.tv.db.impl.entity.episode.EpisodeDb
import anilibria.tv.domain.entity.episode.Episode
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Test

class EpisodeDbDataSourceTest {

    private val dao = mockk<EpisodeDao>()
    private val converter = mockk<EpisodeConverter>()
    private val dataSource = EpisodeDbDataSourceImpl(dao, converter)

    private val dto = listOf<EpisodeDb>(mockk(), mockk())
    private val domain = listOf<Episode>(mockk(), mockk())

    @Test
    fun `getListAll EXPECT success`() {
        every { dao.getListAll() } returns Single.just(dto)
        every { converter.toDomain(dto) } returns domain

        dataSource.getList().test().assertValue(domain)

        verify { dao.getListAll() }
        verify { converter.toDomain(dto) }
        confirmVerified(dao, converter)
    }

    @Test
    fun `getList EXPECT success`() {
        val ids = listOf(1, 2)
        every { dao.getSomeByReleases(ids) } returns Single.just(dto)
        every { converter.toDomain(dto) } returns domain

        dataSource.getSome(ids).test().assertValue(domain)

        verify { dao.getSomeByReleases(ids) }
        verify { converter.toDomain(dto) }
        confirmVerified(dao, converter)
    }

    @Test
    fun `getListByPairIds EXPECT success`() {
        val ids = listOf(1 to 10, 2 to 20)
        val keys = listOf("1_10", "2_20")
        every { dao.getSome(keys) } returns Single.just(dto)
        every { converter.toDbKey(ids) } returns keys
        every { converter.toDomain(dto) } returns domain

        dataSource.getListByPairIds(ids).test().assertValue(domain)

        verify { converter.toDbKey(ids) }
        verify { dao.getSome(keys) }
        verify { converter.toDomain(dto) }
        confirmVerified(dao, converter)
    }

    @Test
    fun `getOne EXPECT success`() {
        val releaseId = 1
        val episodeId = 10
        val dtoItem = dto[0]
        val domainItem = domain[0]
        every { dao.getOne(releaseId, episodeId) } returns Single.just(dtoItem)
        every { converter.toDomain(dtoItem) } returns domainItem

        dataSource.getOne(releaseId, episodeId).test().assertValue(domainItem)

        verify { dao.getOne(releaseId, episodeId) }
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
        every { dao.remove(keys) } returns Completable.complete()
        every { converter.toDbKey(ids) } returns keys

        dataSource.remove(ids).test().assertComplete()

        verify { converter.toDbKey(ids) }
        verify { dao.remove(keys) }
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