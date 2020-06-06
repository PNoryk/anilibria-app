package anilibria.tv.db.impl.converters

import anilibria.tv.db.impl.entity.episode.EpisodeDb
import anilibria.tv.db.impl.entity.favorite.FavoriteDb
import anilibria.tv.db.impl.entity.history.EpisodeHistoryDb
import anilibria.tv.db.impl.entity.history.ReleaseHistoryDb
import anilibria.tv.db.impl.entity.schedule.FlatScheduleDayDb
import anilibria.tv.db.impl.entity.schedule.ScheduleDayDb
import anilibria.tv.db.impl.entity.schedule.ScheduleReleaseDb
import anilibria.tv.domain.entity.episode.Episode
import anilibria.tv.domain.entity.history.EpisodeHistory
import anilibria.tv.domain.entity.relative.EpisodeHistoryRelative
import anilibria.tv.domain.entity.relative.FavoriteRelative
import anilibria.tv.domain.entity.relative.ReleaseHistoryRelative
import anilibria.tv.domain.entity.relative.ScheduleDayRelative
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class ScheduleConverterTest {

    private val converter = ScheduleConverter()

    private val db = listOf(
        ScheduleDayDb(
            scheduleDay = FlatScheduleDayDb(1),
            items = listOf(
                ScheduleReleaseDb(1, 10),
                ScheduleReleaseDb(1, 20),
                ScheduleReleaseDb(1, 30)
            )
        ),
        ScheduleDayDb(
            scheduleDay = FlatScheduleDayDb(2),
            items = listOf(
                ScheduleReleaseDb(2, 40),
                ScheduleReleaseDb(2, 50),
                ScheduleReleaseDb(2, 60)
            )
        )
    )

    private val domain = listOf(
        ScheduleDayRelative(
            dayId = 1,
            releaseIds = listOf(10, 20, 30)
        ),
        ScheduleDayRelative(
            dayId = 2,
            releaseIds = listOf(40, 50, 60)
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