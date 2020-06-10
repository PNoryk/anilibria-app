package anilibria.tv.db.impl.datasource

import anilibria.tv.db.impl.converters.EpisodeHistoryConverter
import anilibria.tv.db.impl.dao.EpisodeHistoryDao
import anilibria.tv.db.impl.entity.history.EpisodeHistoryDb
import anilibria.tv.domain.entity.common.keys.EpisodeKey
import anilibria.tv.domain.entity.relative.EpisodeHistoryRelative
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Test

class EpisodeHistoryDbDataSourceTest {

    private val dao = mockk<EpisodeHistoryDao>()
    private val converter = mockk<EpisodeHistoryConverter>()
    private val dataSource = EpisodeHistoryDbDataSourceImpl(dao, converter)

    private val dto = listOf<EpisodeHistoryDb>(mockk(), mockk())
    private val domain = listOf<EpisodeHistoryRelative>(mockk(), mockk())

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
        val keys = listOf(EpisodeKey(1, 1), EpisodeKey(2, null))
        val dbIds = listOf(2)
        val dbKeys = listOf("1_1")
        every { dao.getSome(dbKeys) } returns Single.just(listOf(dto[0]))
        every { dao.getSomeByReleases(dbIds) } returns Single.just(listOf(dto[1]))
        every { converter.toDbKey(listOf(keys[0])) } returns dbKeys
        every { converter.toDomain(dto) } returns domain

        dataSource.getSome(keys).test().assertValue(domain)

        verify { dao.getSome(dbKeys) }
        verify { dao.getSomeByReleases(dbIds) }
        verify { converter.toDbKey(listOf(keys[0])) }
        verify { converter.toDomain(dto) }
        confirmVerified(dao, converter)
    }

    @Test
    fun `getOne EXPECT success`() {
        val key = EpisodeKey(1, 10)
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
        val keys = listOf(EpisodeKey(1, 10), EpisodeKey(2, 20))
        val dbKeys = listOf("1_10", "2_20")
        every { dao.remove(dbKeys) } returns Completable.complete()
        every { converter.toDbKey(keys) } returns dbKeys

        dataSource.remove(keys).test().assertComplete()

        verify { converter.toDbKey(keys) }
        verify { dao.remove(dbKeys) }
        confirmVerified(dao, converter)
    }

    @Test
    fun `remove WHEN has null episode id EXPECT success`() {
        val keys = listOf(EpisodeKey(1, null))
        val dbKeys = listOf("1_null")
        every { converter.toDbKey(keys) } returns dbKeys
        every { dao.remove(dbKeys) } returns Completable.complete()

        dataSource.remove(keys).test().assertError(IllegalArgumentException::class.java)

        verify { dao.remove(dbKeys) }
        verify { converter.toDbKey(keys) }
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