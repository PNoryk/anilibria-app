package anilibria.tv.db.impl.datasource

import anilibria.tv.db.impl.converters.*
import anilibria.tv.db.impl.dao.*
import anilibria.tv.db.impl.entity.episode.EpisodeDb
import anilibria.tv.db.impl.entity.history.EpisodeHistoryDb
import anilibria.tv.db.impl.entity.history.ReleaseHistoryDb
import anilibria.tv.db.impl.entity.schedule.ScheduleDayDb
import anilibria.tv.db.impl.entity.torrent.TorrentDb
import anilibria.tv.domain.entity.episode.Episode
import anilibria.tv.domain.entity.relative.EpisodeHistoryRelative
import anilibria.tv.domain.entity.relative.ReleaseHistoryRelative
import anilibria.tv.domain.entity.relative.ScheduleDayRelative
import anilibria.tv.domain.entity.torrent.Torrent
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