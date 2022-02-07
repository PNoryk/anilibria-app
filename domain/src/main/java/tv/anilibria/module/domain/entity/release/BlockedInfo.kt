package tv.anilibria.module.domain.entity.release

import tv.anilibria.module.domain.entity.common.HtmlText

data class BlockedInfo(
    val isBlocked: Boolean,
    val reason: HtmlText?
)