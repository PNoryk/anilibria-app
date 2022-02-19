package ru.radiationx.anilibria.model

import tv.anilibria.module.domain.entity.release.ReleaseId

data class ScheduleItemState(
    val releaseId: ReleaseId,
    val posterUrl: String,
    val isCompleted: Boolean
)