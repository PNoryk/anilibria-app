package tv.anilibria.module.data.restapi.datasource.remote.api

import com.squareup.moshi.Moshi
import io.reactivex.Single
import tv.anilibria.module.data.restapi.entity.app.schedule.ScheduleDayResponse
import tv.anilibria.module.data.restapi.entity.mapper.toDomain
import tv.anilibria.module.domain.entity.schedule.ScheduleDay
import tv.anilibria.plugin.data.restapi.ApiNetworkClient
import tv.anilibria.plugin.data.restapi.ApiConfigProvider
import tv.anilibria.plugin.data.restapi.mapApiResponse
import javax.inject.Inject

class ScheduleRemoteDataSource @Inject constructor(
    private val apiClient: ApiNetworkClient,
    private val apiConfig: ApiConfigProvider,
    private val moshi: Moshi
) {

    fun getSchedule(): Single<List<ScheduleDay>> {
        val args = mapOf(
            "query" to "schedule",
            "filter" to "id,torrents,playlist,favorite,moon,blockedInfo",
            "rm" to "true"
        )
        return apiClient
            .post(apiConfig.apiUrl, args)
            .mapApiResponse<List<ScheduleDayResponse>>(moshi)
            .map { items -> items.map { it.toDomain() } }
    }

}