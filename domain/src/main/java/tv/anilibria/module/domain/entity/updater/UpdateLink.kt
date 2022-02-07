package tv.anilibria.module.domain.entity.updater

import tv.anilibria.module.domain.entity.common.AbsoluteUrl

data class UpdateLink(
    val name: String,
    val url: AbsoluteUrl,
    val type: UpdateLinkType,
)