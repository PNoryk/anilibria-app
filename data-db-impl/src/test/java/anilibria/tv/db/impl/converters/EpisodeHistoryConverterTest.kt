package anilibria.tv.db.impl.converters

import anilibria.tv.db.impl.entity.episode.EpisodeDb
import anilibria.tv.db.impl.entity.history.EpisodeHistoryDb
import anilibria.tv.domain.entity.common.keys.EpisodeKey
import anilibria.tv.domain.entity.episode.Episode
import anilibria.tv.domain.entity.history.EpisodeHistory
import anilibria.tv.domain.entity.relative.EpisodeHistoryRelative
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class EpisodeHistoryConverterTest {

    private val converter = EpisodeHistoryConverter()

    private val db = listOf(
        EpisodeHistoryDb(
            key = "10_1",
            releaseId = 10,
            id = 1,
            seek = 9999L,
            lastAccess = Date(65536L),
            isViewed = true
        ),
        EpisodeHistoryDb(
            key = "10_2",
            releaseId = 10,
            id = 2,
            seek = 0,
            lastAccess = Date(0),
            isViewed = false
        )
    )

    private val domain = listOf(
        EpisodeHistoryRelative(
            releaseId = 10,
            id = 1,
            seek = 9999L,
            lastAccess = Date(65536L),
            isViewed = true
        ),
        EpisodeHistoryRelative(
            releaseId = 10,
            id = 2,
            seek = 0,
            lastAccess = Date(0),
            isViewed = false
        )
    )

    @Test
    fun `from db EXPECT domain`() {
        db.map { converter.toDomain(it) }.also {
            assertEquals(domain, it)
        }
        converter.toDomain(db).also {
            assertEquals(domain, it)
        }
    }

    @Test
    fun `from domain EXPECT db`() {
        domain.map { converter.toDb(it) }.also {
            assertEquals(db, it)
        }
        converter.toDb(domain).also {
            assertEquals(db, it)
        }
    }

    @Test
    fun `check db key`() {
        converter.toDbKey(10, 1).also {
            assertEquals("10_1", it)
        }
        converter.toDbKey(listOf(EpisodeKey(10, 1), EpisodeKey(10, 2), EpisodeKey(20, null))).also {
            assertEquals(listOf("10_1", "10_2", "20_null"), it)
        }
    }
}