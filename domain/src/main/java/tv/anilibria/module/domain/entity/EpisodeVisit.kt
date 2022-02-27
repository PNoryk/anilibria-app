package tv.anilibria.module.domain.entity

import kotlinx.datetime.Instant
import tv.anilibria.module.domain.entity.release.EpisodeId

data class EpisodeVisit(
    val id: EpisodeId,
    //todo добавить тип
    val playerSeek: Long?,
    val lastOpenAt: Instant?
) {
    val isViewed = lastOpenAt != null
}