package tv.anilibria.module.domain.entity

import kotlinx.datetime.Instant
import tv.anilibria.module.domain.entity.release.EpisodeId
import tv.anilibria.module.domain.entity.release.ReleaseId

data class EpisodeVisit(
    val id: EpisodeId,
    val playerSeek: Long?,
    val lastOpenAt: Instant?
) {
    val isViewed = lastOpenAt != null
}