package tv.anilibria.module.data.network.entity.app.feed

import tv.anilibria.module.data.network.entity.app.release.ReleaseItem

data class ScheduleItem(
        val releaseItem: ReleaseItem,
        val completed: Boolean = false
)