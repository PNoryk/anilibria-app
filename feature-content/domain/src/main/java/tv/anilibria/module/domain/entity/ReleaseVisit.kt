package tv.anilibria.module.domain.entity

import kotlinx.datetime.Instant
import tv.anilibria.module.domain.entity.release.ReleaseId

data class ReleaseVisit(
    val id: ReleaseId,
    val lastOpenAt: Instant,
)