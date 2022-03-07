package tv.anilibria.feature.content.types

import kotlinx.datetime.Instant
import tv.anilibria.feature.content.types.release.ReleaseId

data class ReleaseUpdate(
    val id: ReleaseId,
    val lastKnownUpdateAt: Instant
)