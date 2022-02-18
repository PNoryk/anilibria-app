package ru.radiationx.data.entity.app.feed

import ru.radiationx.data.entity.app.release.ReleaseItem

@Deprecated("old data")
data class ScheduleItem(
        val releaseItem: ReleaseItem,
        val completed: Boolean = false
)