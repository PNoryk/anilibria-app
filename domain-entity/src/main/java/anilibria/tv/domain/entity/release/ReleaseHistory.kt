package anilibria.tv.domain.entity.release

import anilibria.tv.domain.entity.release.Release
import java.util.*

data class ReleaseHistory(
    val timestamp: Date,
    val release: Release
)