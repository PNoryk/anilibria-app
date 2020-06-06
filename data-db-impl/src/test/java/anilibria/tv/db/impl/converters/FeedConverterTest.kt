package anilibria.tv.db.impl.converters

import anilibria.tv.db.impl.entity.episode.EpisodeDb
import anilibria.tv.db.impl.entity.feed.FeedDb
import anilibria.tv.domain.entity.episode.Episode
import anilibria.tv.domain.entity.feed.Feed
import anilibria.tv.domain.entity.relative.FeedRelative
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test

class FeedConverterTest {

    private val converter = FeedConverter()

    private val db = listOf(
        FeedDb(
            key = "10_null",
            releaseId = 10,
            youtubeId = null
        ),
        FeedDb(
            key = "null_20",
            releaseId = null,
            youtubeId = 20
        )
    )

    private val domain = listOf(
        FeedRelative(
            releaseId = 10,
            youtubeId = null
        ),
        FeedRelative(
            releaseId = null,
            youtubeId = 20
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
        converter.toDbKey(10, null).also {
            assertEquals("10_null", it)
        }
        converter.toDbKey(null, 20).also {
            assertEquals("null_20", it)
        }
        converter.toDbKey(listOf(10 to null, null to 20)).also {
            assertEquals(listOf("10_null", "null_20"), it)
        }
    }
}