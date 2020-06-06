package anilibria.tv.db.impl.converters

import anilibria.tv.db.impl.entity.episode.EpisodeDb
import anilibria.tv.db.impl.entity.favorite.FavoriteDb
import anilibria.tv.db.impl.entity.history.EpisodeHistoryDb
import anilibria.tv.db.impl.entity.history.ReleaseHistoryDb
import anilibria.tv.domain.entity.episode.Episode
import anilibria.tv.domain.entity.history.EpisodeHistory
import anilibria.tv.domain.entity.relative.EpisodeHistoryRelative
import anilibria.tv.domain.entity.relative.FavoriteRelative
import anilibria.tv.domain.entity.relative.ReleaseHistoryRelative
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class FavoriteConverterTest {

    private val converter = FavoriteConverter()

    private val db = listOf(
        FavoriteDb(
            releaseId = 10
        ),
        FavoriteDb(
            releaseId = 20
        )
    )

    private val domain = listOf(
        FavoriteRelative(
            releaseId = 10
        ),
        FavoriteRelative(
            releaseId = 20
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