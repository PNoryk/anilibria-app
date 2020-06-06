package anilibria.tv.db.impl.converters.release

import anilibria.tv.db.impl.converters.ReleaseConverter
import anilibria.tv.db.impl.entity.release.BlockInfoDb
import anilibria.tv.domain.entity.release.BlockInfo
import org.junit.Assert.assertEquals
import org.junit.Test

class BlockInfoConverterTest {

    private val converter = ReleaseConverter()

    private val db = listOf(
        BlockInfoDb(
            releaseId = 20,
            blocked = true,
            reason = null
        ),
        BlockInfoDb(
            releaseId = 20,
            blocked = false,
            reason = "reason"
        )
    )

    private val domain = listOf(
        BlockInfo(
            blocked = true,
            reason = null
        ),
        BlockInfo(
            blocked = false,
            reason = "reason"
        )
    )

    @Test
    fun `from db EXPECT domain`() {
        val actual = db.map { converter.toDomain(it) }
        assertEquals(domain, actual)
    }

    @Test
    fun `from domain EXPECT db`() {
        val actual = domain.map { converter.toDb(20, it) }
        assertEquals(db, actual)
    }
}