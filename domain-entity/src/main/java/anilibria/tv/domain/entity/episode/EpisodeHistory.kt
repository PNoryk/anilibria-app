package anilibria.tv.domain.entity.episode

import anilibria.tv.domain.entity.episode.Episode
import java.util.*

data class EpisodeHistory(
    val seek: Long,
    val lastAccess: Date,
    val isViewed: Boolean,
    val episode: Episode
)