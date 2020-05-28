package ru.radiationx.data.api.datasource

import io.reactivex.Single
import ru.radiationx.data.adomain.entity.schedule.ScheduleDay
import ru.radiationx.data.api.common.handleApiResponse
import ru.radiationx.data.api.converter.ScheduleConverter
import ru.radiationx.data.api.service.ScheduleService
import toothpick.InjectConstructor

@InjectConstructor
class ScheduleApiDataSource(
    private val scheduleService: ScheduleService,
    private val scheduleConverter: ScheduleConverter
) {

    fun getList(): Single<List<ScheduleDay>> = scheduleService
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