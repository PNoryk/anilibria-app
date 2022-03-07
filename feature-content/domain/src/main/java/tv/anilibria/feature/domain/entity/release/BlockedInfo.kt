package tv.anilibria.feature.domain.entity.release

import tv.anilibria.core.types.HtmlText

data class BlockedInfo(
    val isBlocked: Boolean,
    val reason: HtmlText?
)