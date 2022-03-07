package tv.anilibria.feature.player.data.domain

import kotlinx.datetime.Instant
import tv.anilibria.feature.content.types.release.EpisodeId

data class EpisodeVisit(
    val id: EpisodeId,
    //todo добавить тип
    val playerSeek: Long?,
    val lastOpenAt: Instant?
) {
    val isViewed = lastOpenAt != null
}