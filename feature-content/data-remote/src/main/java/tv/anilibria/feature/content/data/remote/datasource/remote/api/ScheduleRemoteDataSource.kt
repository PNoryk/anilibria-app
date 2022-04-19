package tv.anilibria.feature.content.data.remote.datasource.remote.api

import toothpick.InjectConstructor
import tv.anilibria.feature.content.data.remote.datasource.remote.retrofit.ScheduleApiWrapper
import tv.anilibria.feature.content.data.remote.entity.mapper.toDomain
import tv.anilibria.feature.content.types.schedule.ScheduleDay
import tv.anilibria.plugin.data.network.formBodyOf
import tv.anilibria.plugin.data.restapi.handleApiResponse

@InjectConstructor
class ScheduleRemoteDataSource(
    private val scheduleApi: ScheduleApiWrapper
) {

    suspend fun getSchedule(): List<ScheduleDay> {
        val args = formBodyOf(
            "query" to "schedule",
            "filter" to "id,torrents,playlist,favorite,moon,blockedInfo",
            "rm" to "true"
        )
        return scheduleApi.proxy()
            .getSchedule(args)
            .handleApiResponse()
            .map { it.toDomain() }
    }

}