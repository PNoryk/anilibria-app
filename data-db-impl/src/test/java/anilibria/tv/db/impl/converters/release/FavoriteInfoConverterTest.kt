package anilibria.tv.db.impl.converters.release

import anilibria.tv.db.impl.converters.ReleaseConverter
import anilibria.tv.db.impl.entity.release.FavoriteInfoDb
import anilibria.tv.domain.entity.release.FavoriteInfo
import org.junit.Assert.assertEquals
import org.junit.Test

class FavoriteInfoConverterTest {

    private val converter = ReleaseConverter()

    private val db = FavoriteInfoDb(
        releaseId = 20,
        rating = 10,
        added = true
    )

    private val domain = FavoriteInfo(
        rating = 10,
        added = true
    )

    @Test
    fun `from db EXPECT domain`() {
        val actual = converter.toDomain(db)
        assertEquals(domain, actual)
    }

    @Test
    fun `from domain EXPECT db`() {
        val actual = converter.toDb(20, domain)
        assertEquals(db, actual)
    }
}