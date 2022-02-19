package ru.radiationx.anilibria.model

import tv.anilibria.module.domain.entity.release.ReleaseId

data class ReleaseItemState(
    val id: ReleaseId,
    val title: String,
    val description: String,
    val posterUrl: String,
    val isNew: Boolean
)