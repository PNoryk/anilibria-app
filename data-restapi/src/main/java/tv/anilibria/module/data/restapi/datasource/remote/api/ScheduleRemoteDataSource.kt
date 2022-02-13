package tv.anilibria.module.data.restapi.datasource.remote.api

import tv.anilibria.module.data.restapi.datasource.remote.retrofit.ScheduleApi
import tv.anilibria.module.data.restapi.entity.mapper.toDomain
import tv.anilibria.module.domain.entity.schedule.ScheduleDay
import tv.anilibria.plugin.data.network.formBodyOf
import tv.anilibria.plugin.data.network.NetworkWrapper
import tv.anilibria.plugin.data.restapi.handleApiResponse
import javax.inject.Inject

class ScheduleRemoteDataSource @Inject constructor(
    private val scheduleApi: NetworkWrapper<ScheduleApi>
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