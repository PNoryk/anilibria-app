package anilibria.tv.db.impl.converters

import anilibria.tv.db.impl.entity.episode.EpisodeDb
import anilibria.tv.domain.entity.common.keys.EpisodeKey
import anilibria.tv.domain.entity.episode.Episode
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test

class EpisodeConverterTest {

    private val converter = EpisodeConverter()

    private val db = listOf(
        EpisodeDb(
            key = "10_1",
            releaseId = 10,
            id = 1,
            title = "title1",
            sd = "sd1",
            hd = "hd1",
            fullhd = "fullHd1",
            srcSd = "srcSd1",
            srcHd = "srcHd1"
        ),
        EpisodeDb(
            key = "10_2",
            releaseId = 10,
            id = 2,
            title = null,
            sd = null,
            hd = null,
            fullhd = null,
            srcSd = null,
            srcHd = null
        )
    )

    private val domain = listOf(
        Episode(
            releaseId = 10,
            id = 1,
            title = "title1",
            sd = "sd1",
            hd = "hd1",
            fullhd = "fullHd1",
            srcSd = "srcSd1",
            srcHd = "srcHd1"
        ),
        Episode(
            releaseId = 10,
            id = 2,
            title = null,
            sd = null,
            hd = null,
            fullhd = null,
            srcSd = null,
            srcHd = null
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