package tv.anilibria.feature.content.types.release

import tv.anilibria.core.types.HtmlText

data class BlockedInfo(
    val isBlocked: Boolean,
    val reason: HtmlText?
)