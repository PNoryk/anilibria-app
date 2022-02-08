package tv.anilibria.feature.appupdates.data.domain

import tv.anilibria.core.types.AbsoluteUrl

data class UpdateLink(
    val name: String,
    val url: AbsoluteUrl,
    val type: UpdateLinkType,
)