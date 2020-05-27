package ru.radiationx.data.adomain.history

import ru.radiationx.data.adomain.release.Release
import java.util.*

data class HistoryItem(
    val timestamp: Date,
    val release: Release
)