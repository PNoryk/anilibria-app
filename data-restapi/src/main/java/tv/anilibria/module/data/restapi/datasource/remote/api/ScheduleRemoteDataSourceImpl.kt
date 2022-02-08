package tv.anilibria.module.data.restapi.datasource.remote.api

import com.squareup.moshi.Moshi
import io.reactivex.Single
import tv.anilibria.module.data.restapi.ApiClient
import tv.anilibria.module.data.network.IClient
import tv.anilibria.module.data.restapi.datasource.remote.ApiConfigProvider
import tv.anilibria.module.data.restapi.datasource.remote.mapApiResponse
import tv.anilibria.module.data.restapi.entity.app.schedule.ScheduleDayResponse
import tv.anilibria.module.data.restapi.entity.mapper.toDomain
import tv.anilibria.module.domain.entity.schedule.ScheduleDay
import tv.anilibria.module.domain.remote.ScheduleRemoteDataSource
import javax.inject.Inject

class ScheduleRemoteDataSourceImpl @Inject constructor(
    @ApiClient private val client: IClient,
    private val apiConfig: ApiConfigProvider,
    private val moshi: Moshi
) : ScheduleRemoteDataSource {

    override fun getSchedule(): Single<List<ScheduleDay>> {
        val args = mapOf(
            "query" to "schedule",
            "filter" to "id,torrents,playlist,favorite,moon,blockedInfo",
            "rm" to "true"
        )
        return client
            .post(apiConfig.apiUrl, args)
            .mapApiResponse<List<ScheduleDayResponse>>(moshi)
            .map { items -> items.map { it.toDomain() } }
    }

}