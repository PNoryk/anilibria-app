package anilibria.tv.domain.entity.converter

import anilibria.tv.domain.entity.episode.Episode
import anilibria.tv.domain.entity.feed.Feed
import anilibria.tv.domain.entity.history.EpisodeHistory
import anilibria.tv.domain.entity.relative.EpisodeHistoryRelative
import anilibria.tv.domain.entity.relative.FavoriteRelative
import anilibria.tv.domain.entity.relative.FeedRelative
import anilibria.tv.domain.entity.release.Release
import anilibria.tv.domain.entity.youtube.Youtube
import toothpick.InjectConstructor

@InjectConstructor
class FeedRelativeConverter {

    fun toRelative(source: Feed) = FeedRelative(
        releaseId = source.release?.id,
        youtubeId = source.youtube?.id
    )

    fun fromRelative(source: FeedRelative, releaseItems: List<Release>, youtubeItems: List<Youtube>): Feed? {
        val release = source.releaseId?.let { releaseId ->
            releaseItems.firstOrNull { it.id == releaseId }
        }
        val youtube = source.youtubeId?.let { youtubeId ->
            youtubeItems.firstOrNull { it.id == youtubeId }
        }
        return if (release != null || youtube != null) {
            Feed(release, youtube)
        } else {
            null
        }
    }
}