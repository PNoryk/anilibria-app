package tv.anilibria.module.data.network.datasource.remote.api

import com.squareup.moshi.Moshi
import io.reactivex.Single
import org.json.JSONArray
import tv.anilibria.module.data.network.ApiClient
import tv.anilibria.module.data.network.datasource.remote.ApiResponse
import tv.anilibria.module.data.network.datasource.remote.IClient
import tv.anilibria.module.data.network.datasource.remote.address.ApiConfigProvider
import tv.anilibria.module.data.network.datasource.remote.mapApiResponse
import tv.anilibria.module.data.network.datasource.remote.parsers.ReleaseParser
import tv.anilibria.module.data.network.datasource.remote.parsers.ScheduleParser
import tv.anilibria.module.data.network.entity.app.schedule.ScheduleDayResponse
import javax.inject.Inject

class ScheduleApi @Inject constructor(
    @ApiClient private val client: IClient,
    private val releaseParser: ReleaseParser,
    private val scheduleParser: ScheduleParser,
    private val apiConfig: ApiConfigProvider,
    private val moshi: Moshi
) {

    fun getSchedule(): Single<List<ScheduleDayResponse>> {
        val args: MutableMap<String, String> = mutableMapOf(
            "query" to "schedule",
            "filter" to "id,torrents,playlist,favorite,moon,blockedInfo",
            "rm" to "true"
        )
        return client
            .post(apiConfig.apiUrl, args)
            .mapApiResponse(moshi)
    }

}