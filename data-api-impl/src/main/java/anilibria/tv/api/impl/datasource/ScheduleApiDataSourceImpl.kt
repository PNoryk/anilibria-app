package anilibria.tv.api.impl.datasource

import io.reactivex.Single
import anilibria.tv.domain.entity.schedule.ScheduleDay
import anilibria.tv.api.impl.common.handleApiResponse
import anilibria.tv.api.impl.converter.ScheduleConverter
import anilibria.tv.api.ScheduleApiDataSource
import anilibria.tv.api.impl.service.ScheduleService
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