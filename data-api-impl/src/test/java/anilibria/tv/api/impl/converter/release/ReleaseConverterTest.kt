package anilibria.tv.api.impl.converter.release

import anilibria.tv.api.impl.converter.DayConverter
import anilibria.tv.api.impl.converter.ReleaseConverter
import anilibria.tv.api.impl.common.DummyHtmlUnescapeTool
import anilibria.tv.api.impl.entity.release.*
import anilibria.tv.domain.entity.episode.Episode
import anilibria.tv.domain.entity.release.BlockInfo
import anilibria.tv.domain.entity.release.FavoriteInfo
import anilibria.tv.domain.entity.release.RandomRelease
import anilibria.tv.domain.entity.release.Release
import anilibria.tv.domain.entity.torrent.Torrent
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class ReleaseConverterTest {

    private val unescapeTool = DummyHtmlUnescapeTool()
    private val dayConverter = DayConverter()
    private val converter = ReleaseConverter(dayConverter, unescapeTool)

    @Test
    fun `from RandomRelease response EXPECT domain`() {
        val response = RandomReleaseResponse(
            code = "code"
        )
        val domain = RandomRelease(
            code = "code"
        )
        val actual = converter.toDomain(response)
        assertEquals(domain, actual)
    }

    @Test
    fun `from all null Release response EXPECT domain`() {
        val response = createNullReleaseResponse(10)
        val domain = createNullRelease(10)
        val actual = converter.toDomain(response)
        assertEquals(domain, actual)
    }

    @Test
    fun `from full Release response EXPECT domain`() {
        val response = createReleaseResponse(10)
        val domain = createRelease(10)
        val actual = converter.toDomain(response)
        assertEquals(domain, actual)
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
        playlist = listOf(createEpisode(id, 1), createEpisode(id, 2)),
        torrents = listOf(createTorrent(id, 1), createTorrent(id, 2)),
        showDonateDialog = true
    )

    private fun createEpisode(releaseId: Int, id: Int) = Episode(
        releaseId = releaseId,
        id = id,
        title = "title$releaseId$id",
        sd = "sd$releaseId$id",
        hd = "hd$releaseId$id",
        fullhd = "fullHd$releaseId$id",
        srcSd = "srcSd$releaseId$id",
        srcHd = "srcHd$releaseId$id"
    )

    private fun createTorrent(releaseId: Int, id: Int) = Torrent(
        releaseId = releaseId,
        id = id,
        hash = "$releaseId$id",
        leechers = 10,
        seeders = 20,
        completed = 30,
        quality = "quality$releaseId$id",
        series = "series$releaseId$id",
        size = 718293L,
        time = Date(12345000L),
        url = "url$releaseId$id"
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

    private fun createReleaseResponse(id: Int) = ReleaseResponse(
        id = id,
        code = "code$id",
        names = listOf(
            "nameRu$id",
            "nameEn$id"
        ),
        series = "series$id",
        poster = "poster$id",
        favorite = FavoriteInfoResponse(10, true),
        last = "2444",
        moon = "webPlayer$id",
        statusCode = "2",
        type = "type$id",
        genres = listOf("genre1", "genre2"),
        voices = listOf("voice1", "voice2"),
        year = "year$id",
        day = "2",
        description = "description$id",
        announce = "announce$id",
        blockedInfo = BlockInfoResponse(false, "reason"),
        playlist = listOf(createEpisodeResponse(id, 1), createEpisodeResponse(id, 2)),
        torrents = listOf(createTorrentResponse(id, 1), createTorrentResponse(id, 2)),
        showDonateDialog = true
    )

    private fun createEpisodeResponse(releaseId: Int, id: Int) = EpisodeResponse(
        id = id,
        title = "title$releaseId$id",
        sd = "sd$releaseId$id",
        hd = "hd$releaseId$id",
        fullhd = "fullHd$releaseId$id",
        srcSd = "srcSd$releaseId$id",
        srcHd = "srcHd$releaseId$id"
    )

    private fun createTorrentResponse(releaseId: Int, id: Int) = TorrentResponse(
        id = id,
        hash = "$releaseId$id",
        leechers = 10,
        seeders = 20,
        completed = 30,
        quality = "quality$releaseId$id",
        series = "series$releaseId$id",
        size = 718293L,
        time = 12345L,
        url = "url$releaseId$id"
    )

    private fun createNullReleaseResponse(id: Int) = ReleaseResponse(
        id = id,
        code = null,
        names = null,
        series = null,
        poster = null,
        favorite = null,
        last = null,
        moon = null,
        statusCode = null,
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
}