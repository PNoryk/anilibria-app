package tv.anilibria.module.domain.entity.updater

import tv.anilibria.core.types.AbsoluteUrl

data class UpdateLink(
    val name: String,
    val url: AbsoluteUrl,
    val type: UpdateLinkType,
)