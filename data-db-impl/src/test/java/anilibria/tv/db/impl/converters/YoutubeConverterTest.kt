package anilibria.tv.db.impl.converters

import anilibria.tv.db.impl.entity.episode.EpisodeDb
import anilibria.tv.db.impl.entity.favorite.FavoriteDb
import anilibria.tv.db.impl.entity.history.EpisodeHistoryDb
import anilibria.tv.db.impl.entity.history.ReleaseHistoryDb
import anilibria.tv.db.impl.entity.youtube.YoutubeDb
import anilibria.tv.domain.entity.episode.Episode
import anilibria.tv.domain.entity.history.EpisodeHistory
import anilibria.tv.domain.entity.relative.EpisodeHistoryRelative
import anilibria.tv.domain.entity.relative.FavoriteRelative
import anilibria.tv.domain.entity.relative.ReleaseHistoryRelative
import anilibria.tv.domain.entity.youtube.Youtube
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class YoutubeConverterTest {

    private val converter = YoutubeConverter()

    private val db = listOf(
        YoutubeDb(
            id = 10,
            title = "title",
            image = "image",
            vid = "vid",
            views = 1000,
            comments = 2000,
            timestamp = Date(5432000L)
        ),
        YoutubeDb(
            id = 20,
            title = null,
            image = null,
            vid = null,
            views = 0,
            comments = 0,
            timestamp = Date(0L)
        )
    )

    private val domain = listOf(
        Youtube(
            id = 10,
            title = "title",
            image = "image",
            vid = "vid",
            views = 1000,
            comments = 2000,
            timestamp = Date(5432000L)
        ),
        Youtube(
            id = 20,
            title = null,
            image = null,
            vid = null,
            views = 0,
            comments = 0,
            timestamp = Date(0L)
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