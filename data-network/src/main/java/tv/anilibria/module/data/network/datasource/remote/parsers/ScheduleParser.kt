package tv.anilibria.module.data.network.datasource.remote.parsers

import org.json.JSONArray
import tv.anilibria.module.data.network.datasource.remote.IApiUtils
import tv.anilibria.module.data.network.entity.app.feed.ScheduleItem
import tv.anilibria.module.data.network.entity.app.schedule.ScheduleDayResponse
import javax.inject.Inject

class ScheduleParser @Inject constructor(
        private val apiUtils: IApiUtils
) {

    fun schedule(jsonResponse: JSONArray, releaseParser: ReleaseParser): List<ScheduleDayResponse> {
        val result = mutableListOf<ScheduleDayResponse>()
        for (i in 0 until jsonResponse.length()) {
            val jsonItem = jsonResponse.getJSONObject(i)
            val releases = releaseParser.releases(jsonItem.getJSONArray("items"))
            val strDay = jsonItem.getString("day")
            val item = ScheduleDayResponse(
                    ScheduleDayResponse.toCalendarDay(strDay),
                    releases
            )
            result.add(item)
        }
        return result
    }
}