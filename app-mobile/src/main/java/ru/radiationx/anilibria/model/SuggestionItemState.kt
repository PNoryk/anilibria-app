package ru.radiationx.anilibria.model

import tv.anilibria.module.domain.entity.release.ReleaseId

data class SuggestionItemState(
    val id: ReleaseId,
    val title: String,
    val poster: String,
    val matchRanges: List<IntRange>
)