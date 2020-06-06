package anilibria.tv.db.impl.converters.release

import anilibria.tv.db.impl.converters.ReleaseConverter
import anilibria.tv.db.impl.entity.release.BlockInfoDb
import anilibria.tv.db.impl.entity.release.FavoriteInfoDb
import anilibria.tv.db.impl.entity.release.FlatReleaseDb
import anilibria.tv.db.impl.entity.release.ReleaseDb
import anilibria.tv.domain.entity.release.BlockInfo
import anilibria.tv.domain.entity.release.FavoriteInfo
import anilibria.tv.domain.entity.release.Release
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class ReleaseConverterTest {

    private val converter = ReleaseConverter()

    @Test
    fun `from all null db EXPECT domain`() {
        val db = createNullReleaseDb(10)
        val domain = createNullRelease(10)
        val actual = converter.toDomain(db)
        assertEquals(domain, actual)
    }

    @Test
    fun `from full db EXPECT domain`() {
        val db = createReleaseDb(10)
        val domain = createRelease(10)
        val actual = converter.toDomain(db)
        assertEquals(domain, actual)
    }

    @Test
    fun `from all null domain EXPECT db`() {
        val db = createNullReleaseDb(10)
        val domain = createNullRelease(10)
        val actual = converter.toDb(domain)
        assertEquals(db, actual)
    }

    @Test
    fun `from full domain EXPECT db`() {
        val db = createReleaseDb(10)
        val domain = createRelease(10)
        val actual = converter.toDb(domain)
        assertEquals(db, actual)
    }

    private fun createRelease(id: Int) = Release(
        id = id,
        code = "code$id",
        nameRu = "nameRu$id",
        nameEn = "nameEn$id",
        series = "series$id",
        poster = "poster$id",
        favorite = FavoriteInfo(10, true),
        last = Date(2444000L),
        webPlayer = "webPlayer$id",
        status = Release.Status.COMPLETE,
        type = "type$id",
        genres = listOf("genre1", "genre2"),
        voices = listOf("voice1", "voice2"),
        year = "year$id",
        day = 3,
        description = "description$id",
        announce = "announce$id",
        blockedInfo = BlockInfo(false, "reason"),
        playlist = null,
        torrents = null,
        showDonateDialog = true
    )

    private fun createNullRelease(id: Int) = Release(
        id = id,
        code = null,
        nameRu = null,
        nameEn = null,
        series = null,
        poster = null,
        favorite = null,
        last = null,
        webPlayer = null,
        status = null,
        type = null,
        genres = null,
        voices = null,
        year = null,
        day = null,
        description = null,
        announce = null,
        blockedInfo = null,
        playlist = null,
        torrents = null,
        showDonateDialog = null
    )

    private fun createReleaseDb(id: Int) = ReleaseDb(
        release = createFlatReleaseDb(id),
        favorite = FavoriteInfoDb(id, 10, true),
        blockedInfo = BlockInfoDb(id, false, "reason")
    )

    private fun createNullReleaseDb(id: Int) = ReleaseDb(
        release = createNullFlatReleaseDb(id),
        favorite = null,
        blockedInfo = null
    )

    private fun createFlatReleaseDb(id: Int) = FlatReleaseDb(
        id = id,
        code = "code$id",
        nameRu = "nameRu$id",
        nameEn = "nameEn$id",
        series = "series$id",
        poster = "poster$id",
        last = Date(2444000L),
        webPlayer = "webPlayer$id",
        status = Release.Status.COMPLETE,
        type = "type$id",
        genres = listOf("genre1", "genre2"),
        voices = listOf("voice1", "voice2"),
        year = "year$id",
        day = 3,
        description = "description$id",
        announce = "announce$id",
        showDonateDialog = true
    )

    private fun createNullFlatReleaseDb(id: Int) = FlatReleaseDb(
        id = id,
        code = null,
        nameRu = null,
        nameEn = null,
        series = null,
        poster = null,
        last = null,
        webPlayer = null,
        status = null,
        type = null,
        genres = null,
        voices = null,
        year = null,
        day = null,
        description = null,
        announce = null,
        showDonateDialog = null
    )
}