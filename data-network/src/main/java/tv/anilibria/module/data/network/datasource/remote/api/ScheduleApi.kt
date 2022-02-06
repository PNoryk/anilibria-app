package tv.anilibria.module.data.network.datasource.remote.api

import io.reactivex.Single
import org.json.JSONArray
import tv.anilibria.module.data.network.ApiClient
import tv.anilibria.module.data.network.datasource.remote.ApiResponse
import tv.anilibria.module.data.network.datasource.remote.IClient
import tv.anilibria.module.data.network.datasource.remote.address.ApiConfig
import tv.anilibria.module.data.network.datasource.remote.parsers.ReleaseParser
import tv.anilibria.module.data.network.datasource.remote.parsers.ScheduleParser
import tv.anilibria.module.data.network.entity.app.schedule.ScheduleDay
import javax.inject.Inject

class ScheduleApi @Inject constructor(
        @ApiClient private val client: IClient,
        private val releaseParser: ReleaseParser,
        private val scheduleParser: ScheduleParser,
        private val apiConfig: ApiConfig
) {

    fun getSchedule(): Single<List<ScheduleDay>> {
        val args: MutableMap<String, String> = mutableMapOf(
                "query" to "schedule",
                "filter" to "id,torrents,playlist,favorite,moon,blockedInfo",
                "rm" to "true"
        )
        return client.post(apiConfig.apiUrl, args)
                .compose(ApiResponse.fetchResult<JSONArray>())
                .map { scheduleParser.schedule(it, releaseParser) }
    }

}