package anilibria.tv.domain.entity.converter

import anilibria.tv.domain.entity.episode.Episode
import anilibria.tv.domain.entity.history.EpisodeHistory
import anilibria.tv.domain.entity.relative.EpisodeHistoryRelative
import anilibria.tv.domain.entity.relative.FavoriteRelative
import anilibria.tv.domain.entity.release.Release
import toothpick.InjectConstructor

@InjectConstructor
class FavoriteRelativeConverter {

    fun toRelative(source: Release) = FavoriteRelative(
        releaseId = source.id
    )

    fun fromRelative(source: FavoriteRelative, releaseItems: List<Release>): Release? {
        return releaseItems.firstOrNull { it.id == source.releaseId }
    }
}