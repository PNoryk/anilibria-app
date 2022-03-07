package ru.radiationx.anilibria.model

import tv.anilibria.core.types.AbsoluteUrl
import tv.anilibria.feature.domain.entity.release.ReleaseId

data class ScheduleItemState(
    val releaseId: ReleaseId,
    val posterUrl: AbsoluteUrl?,
    val isCompleted: Boolean
)