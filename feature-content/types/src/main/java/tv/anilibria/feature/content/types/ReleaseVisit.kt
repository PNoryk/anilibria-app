package tv.anilibria.feature.content.types

import kotlinx.datetime.Instant
import tv.anilibria.feature.content.types.release.ReleaseId

data class ReleaseVisit(
    val id: ReleaseId,
    val lastOpenAt: Instant,
)