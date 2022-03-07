package ru.radiationx.anilibria.model

import tv.anilibria.core.types.AbsoluteUrl
import tv.anilibria.feature.content.types.release.ReleaseId

data class SuggestionItemState(
    val id: ReleaseId,
    val title: String,
    val poster: AbsoluteUrl?,
    val matchRanges: List<IntRange>
)