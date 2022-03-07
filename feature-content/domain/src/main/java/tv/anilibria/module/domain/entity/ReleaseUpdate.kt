package tv.anilibria.module.domain.entity

import kotlinx.datetime.Instant
import tv.anilibria.module.domain.entity.release.ReleaseId

data class ReleaseUpdate(
    val id: ReleaseId,
    val lastKnownUpdateAt: Instant
)