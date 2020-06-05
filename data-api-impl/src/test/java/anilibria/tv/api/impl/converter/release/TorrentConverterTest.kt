package anilibria.tv.api.impl.converter.release

import anilibria.tv.api.common.HtmlUnescapeTool
import anilibria.tv.api.impl.converter.DayConverter
import anilibria.tv.api.impl.converter.ReleaseConverter
import anilibria.tv.api.impl.entity.checker.UpdateLinkResponse
import anilibria.tv.api.impl.entity.checker.UpdateResponse
import anilibria.tv.api.impl.entity.comments.CommentsInfoResponse
import anilibria.tv.api.impl.entity.release.BlockInfoResponse
import anilibria.tv.api.impl.entity.release.EpisodeResponse
import anilibria.tv.api.impl.entity.release.FavoriteInfoResponse
import anilibria.tv.api.impl.entity.release.TorrentResponse
import anilibria.tv.domain.entity.checker.Update
import anilibria.tv.domain.entity.checker.UpdateLink
import anilibria.tv.domain.entity.comments.CommentsInfo
import anilibria.tv.domain.entity.episode.Episode
import anilibria.tv.domain.entity.release.BlockInfo
import anilibria.tv.domain.entity.release.FavoriteInfo
import anilibria.tv.domain.entity.torrent.Torrent
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class TorrentConverterTest {

    private val unescapeTool = mockk<HtmlUnescapeTool>()
    private val dayConverter = DayConverter()
    private val converter = ReleaseConverter(dayConverter, unescapeTool)

    private val response = listOf(
        TorrentResponse(
            id = 1,
            hash = "hash1",
            leechers = 1000,
            seeders = 2000,
            completed = 3000,
            quality = "quality1",
            series = "series1",
            size = 123456789L,
            time = 124L,
            url = "url1"
        ),
        TorrentResponse(
            id = 2,
            hash = null,
            leechers = 0,
            seeders = 0,
            completed = 0,
            quality = null,
            series = null,
            size = 0L,
            time = 0L,
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
    fun `from response EXPECT domain`() {
        val actual = response.map { converter.toDomain(10, it) }
        assertEquals(domain, actual)
    }

}