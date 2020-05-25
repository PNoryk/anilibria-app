package ru.radiationx.data.api.service.schedule

import io.reactivex.Single
import ru.radiationx.data.adomain.ScheduleDay
import ru.radiationx.data.api.common.handleApiResponse
import ru.radiationx.data.api.converter.ScheduleConverter
import toothpick.InjectConstructor

@InjectConstructor
class ScheduleService(
    private val scheduleApi: ScheduleApi,
    private val scheduleConverter: ScheduleConverter
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
        .map {
            it.map { dayResponse ->
                scheduleConverter.toDomain(dayResponse)
            }
        }
}