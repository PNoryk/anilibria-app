package tv.anilibria.feature.domain.entity

import kotlinx.datetime.Instant
import tv.anilibria.feature.domain.entity.release.ReleaseId

data class ReleaseVisit(
    val id: ReleaseId,
    val lastOpenAt: Instant,
)