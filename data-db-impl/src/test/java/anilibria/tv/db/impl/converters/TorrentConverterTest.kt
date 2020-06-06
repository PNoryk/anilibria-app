package anilibria.tv.db.impl.converters

import anilibria.tv.db.impl.entity.episode.EpisodeDb
import anilibria.tv.db.impl.entity.torrent.TorrentDb
import anilibria.tv.domain.entity.episode.Episode
import anilibria.tv.domain.entity.torrent.Torrent
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class TorrentConverterTest {

    private val converter = TorrentConverter()

    private val db = listOf(
        TorrentDb(
            key = "10_1",
            releaseId = 10,
            id = 1,
            hash = "hash1",
            leechers = 1000,
            seeders = 2000,
            completed = 3000,
            quality = "quality1",
            series = "series1",
            size = 123456789L,
            time = Date(124000L),
            url = "url1"
        ),
        TorrentDb(
            key = "10_2",
            releaseId = 10,
            id = 2,
            hash = null,
            leechers = 0,
            seeders = 0,
            completed = 0,
            quality = null,
            series = null,
            size = 0L,
            time = Date(0L),
            url = null
        )
    )

    private val domain = listOf(
        Torrent(
            releaseId = 10,
            id = 1,
            hash = "hash1",
            leechers = 1000,
            seeders = 2000,
            completed = 3000,
            quality = "quality1",
            series = "series1",
            size = 123456789L,
            time = Date(124000L),
            url = "url1"
        ),
        Torrent(
            releaseId = 10,
            id = 2,
            hash = null,
            leechers = 0,
            seeders = 0,
            completed = 0,
            quality = null,
            series = null,
            size = 0L,
            time = Date(0),
            url = null
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
        converter.toDbKey(listOf(10 to 1, 10 to 2)).also {
            assertEquals(listOf("10_1", "10_2"), it)
        }
    }
}