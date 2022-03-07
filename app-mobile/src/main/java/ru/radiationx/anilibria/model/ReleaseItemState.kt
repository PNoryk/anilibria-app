package ru.radiationx.anilibria.model

import tv.anilibria.core.types.AbsoluteUrl
import tv.anilibria.feature.content.types.release.ReleaseId

data class ReleaseItemState(
    val id: ReleaseId,
    val title: String,
    val description: String,
    val posterUrl: AbsoluteUrl?,
    val isNew: Boolean
)