package anilibria.tv.db.impl.converters

import anilibria.tv.db.impl.entity.episode.EpisodeDb
import anilibria.tv.db.impl.entity.history.EpisodeHistoryDb
import anilibria.tv.db.impl.entity.history.ReleaseHistoryDb
import anilibria.tv.domain.entity.episode.Episode
import anilibria.tv.domain.entity.history.EpisodeHistory
import anilibria.tv.domain.entity.relative.EpisodeHistoryRelative
import anilibria.tv.domain.entity.relative.ReleaseHistoryRelative
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class ReleaseHistoryConverterTest {

    private val converter = ReleaseHistoryConverter()

    private val db = listOf(
        ReleaseHistoryDb(
            releaseId = 10,
            timestamp = Date(65536L)
        ),
        ReleaseHistoryDb(
            releaseId = 20,
            timestamp = Date(0)
        )
    )

    private val domain = listOf(
        ReleaseHistoryRelative(
            releaseId = 10,
            timestamp = Date(65536L)
        ),
        ReleaseHistoryRelative(
            releaseId = 20,
            timestamp = Date(0)
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
}