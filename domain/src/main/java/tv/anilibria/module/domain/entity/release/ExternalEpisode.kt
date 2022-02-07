package tv.anilibria.module.domain.entity.release

import tv.anilibria.module.domain.entity.common.AbsoluteUrl

data class ExternalEpisode(
    val id: Int,
    val title: String?,
    val url: AbsoluteUrl?
)