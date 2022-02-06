package tv.anilibria.module.data.network.entity.app.schedule

import tv.anilibria.module.data.network.entity.app.release.ReleaseResponse

data class ScheduleDayResponse(
    val day: String,
    val items: List<ReleaseResponse>
)