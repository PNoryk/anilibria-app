package anilibria.tv.domain.entity.converter

import anilibria.tv.domain.entity.episode.Episode
import anilibria.tv.domain.entity.history.EpisodeHistory
import anilibria.tv.domain.entity.relative.EpisodeHistoryRelative
import toothpick.InjectConstructor

@InjectConstructor
class EpisodeHistoryRelativeConverter {

    fun toRelative(source: EpisodeHistory) = EpisodeHistoryRelative(
        releaseId = source.episode.releaseId,
        id = source.episode.id,
        seek = source.seek,
        lastAccess = source.lastAccess,
        isViewed = source.isViewed
    )

    fun fromRelative(source: EpisodeHistoryRelative, episode: Episode) = EpisodeHistory(
        seek = source.seek,
        lastAccess = source.lastAccess,
        isViewed = source.isViewed,
        episode = episode
    )

    fun fromRelative(source: EpisodeHistoryRelative, episodeItems: List<Episode>): EpisodeHistory? {
        val episode = episodeItems.firstOrNull {
            it.releaseId == source.releaseId && it.id == source.id
        }
        return episode?.let { fromRelative(source, it) }
    }
}