package tv.anilibria.feature.domain.entity

import kotlinx.datetime.Instant
import tv.anilibria.feature.domain.entity.release.ReleaseId

data class ReleaseUpdate(
    val id: ReleaseId,
    val lastKnownUpdateAt: Instant
)