package ru.radiationx.data.adomain.entity.history

import ru.radiationx.data.adomain.entity.release.Release
import java.util.*

data class HistoryItem(
    val timestamp: Date,
    val release: Release
)