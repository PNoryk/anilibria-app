package tv.anilibria.module.domain.entity.release

import tv.anilibria.core.types.AbsoluteUrl

data class ExternalEpisode(
    val id: Long,
    val title: String?,
    val url: AbsoluteUrl?
)