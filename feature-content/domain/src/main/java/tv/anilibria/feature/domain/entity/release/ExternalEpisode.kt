package tv.anilibria.feature.domain.entity.release

import tv.anilibria.core.types.AbsoluteUrl

data class ExternalEpisode(
    val id: EpisodeId,
    val title: String?,
    val url: AbsoluteUrl?
)