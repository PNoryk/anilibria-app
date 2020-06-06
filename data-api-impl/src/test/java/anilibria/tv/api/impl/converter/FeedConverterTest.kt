package anilibria.tv.api.impl.converter

import anilibria.tv.api.impl.entity.feed.FeedItemResponse
import anilibria.tv.api.impl.entity.release.ReleaseResponse
import anilibria.tv.api.impl.entity.youtube.YouTubeResponse
import anilibria.tv.domain.entity.feed.Feed
import anilibria.tv.domain.entity.release.Release
import anilibria.tv.domain.entity.youtube.Youtube
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test

class FeedConverterTest {

    private val releaseConverter = mockk<ReleaseConverter>()
    private val youtubeConverter = mockk<YoutubeConverter>()
    private val converter = FeedConverter(releaseConverter, youtubeConverter)

    private val releaseResponse = mockk<ReleaseResponse>()
    private val releaseDomain = mockk<Release>()

    private val youtubeResponse = mockk<YouTubeResponse>()
    private val youtubeDomain = mockk<Youtube>()

    @Test
    fun `from response with release EXPECT domain`() {
        val response = FeedItemResponse(
            release = releaseResponse,
            youtube = null
        )
        val domain = Feed(
            release = releaseDomain,
            youtube = null
        )
        every { releaseConverter.toDomain(releaseResponse) } returns releaseDomain

        val actual = converter.toDomain(response)

        assertEquals(domain, actual)
        verify { releaseConverter.toDomain(releaseResponse) }
        verify(inverse = true) { youtubeConverter.toDomain(youtubeResponse) }
        confirmVerified(releaseConverter, youtubeConverter)
    }

    @Test
    fun `from response with youtube EXPECT domain`() {
        val response = FeedItemResponse(
            release = null,
            youtube = youtubeResponse
        )
        val domain = Feed(
            release = null,
            youtube = youtubeDomain
        )
        every { youtubeConverter.toDomain(youtubeResponse) } returns youtubeDomain

        val actual = converter.toDomain(response)

        assertEquals(domain, actual)
        verify { youtubeConverter.toDomain(youtubeResponse) }
        verify(inverse = true) { releaseConverter.toDomain(releaseResponse) }
        confirmVerified(releaseConverter, youtubeConverter)
    }

    @Test
    fun `from response all null EXPECT domain`() {
        val response = FeedItemResponse(
            release = null,
            youtube = null
        )
        val domain = Feed(
            release = null,
            youtube = null
        )

        val actual = converter.toDomain(response)

        assertEquals(domain, actual)
        verify(inverse = true) { youtubeConverter.toDomain(youtubeResponse) }
        verify(inverse = true) { releaseConverter.toDomain(releaseResponse) }
        confirmVerified(releaseConverter, youtubeConverter)
    }
}