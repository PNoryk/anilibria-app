package ru.radiationx.data.api.service.schedule

import io.reactivex.Single
import ru.radiationx.data.api.common.handleApiResponse
import ru.radiationx.data.entity.app.schedule.ScheduleDay

class ScheduleService(
    private val scheduleApi: ScheduleApi
) {

    fun getList(): Single<List<ScheduleDay>> = scheduleApi
        .getList(
            mapOf(
                "query" to "schedule",
                "filter" to "id,torrents,playlist,favorite,moon,blockedInfo",
                "rm" to "true"
            )
        )
        .handleApiResponse()
}