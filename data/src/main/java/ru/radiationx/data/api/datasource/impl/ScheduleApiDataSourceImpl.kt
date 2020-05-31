package ru.radiationx.data.api.datasource.impl

import io.reactivex.Single
import anilibria.tv.domain.entity.schedule.ScheduleDay
import ru.radiationx.data.api.common.handleApiResponse
import ru.radiationx.data.api.converter.ScheduleConverter
import ru.radiationx.data.api.datasource.ScheduleApiDataSource
import ru.radiationx.data.api.service.ScheduleService
import toothpick.InjectConstructor

@InjectConstructor
class ScheduleApiDataSourceImpl(
    private val scheduleService: ScheduleService,
    private val scheduleConverter: ScheduleConverter
) : ScheduleApiDataSource {

    override fun getList(): Single<List<ScheduleDay>> = scheduleService
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