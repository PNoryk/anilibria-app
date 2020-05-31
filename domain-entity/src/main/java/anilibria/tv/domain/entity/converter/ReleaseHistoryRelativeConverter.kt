package anilibria.tv.domain.entity.converter

import anilibria.tv.domain.entity.episode.Episode
import anilibria.tv.domain.entity.history.ReleaseHistory
import anilibria.tv.domain.entity.relative.ReleaseHistoryRelative
import anilibria.tv.domain.entity.release.Release
import toothpick.InjectConstructor

@InjectConstructor
class ReleaseHistoryRelativeConverter {

    fun toRelative(source: ReleaseHistory) = ReleaseHistoryRelative(
        releaseId = source.release.id,
        timestamp = source.timestamp
    )

    fun fromRelative(source: ReleaseHistoryRelative, release: Release) = ReleaseHistory(
        timestamp = source.timestamp,
        release = release
    )

    fun fromRelative(source: ReleaseHistoryRelative, releaseItems: List<Release>): ReleaseHistory? {
        val release = releaseItems.firstOrNull { it.id == source.releaseId }
        return release?.let { fromRelative(source, it) }
    }
}