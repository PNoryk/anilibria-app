package anilibria.tv.domain.entity.relative

import anilibria.tv.domain.entity.episode.Episode
import java.util.*

data class EpisodeHistoryRelative(
    val releaseId: Int,
    val id: Int,
    val seek: Long,
    val lastAccess: Date,
    val isViewed: Boolean
)