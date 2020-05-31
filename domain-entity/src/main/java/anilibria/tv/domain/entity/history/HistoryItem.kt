package anilibria.tv.domain.entity.history

import anilibria.tv.domain.entity.release.Release
import java.util.*

data class HistoryItem(
    val timestamp: Date,
    val release: Release
)