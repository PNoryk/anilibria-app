package tv.anilibria.module.data.network.datasource.remote.parsers

import org.json.JSONArray
import ru.radiationx.shared.ktx.android.mapObjects
import tv.anilibria.module.data.network.entity.app.schedule.ScheduleDayResponse
import javax.inject.Inject

class ScheduleParser @Inject constructor() {

    fun schedule(jsonResponse: JSONArray, releaseParser: ReleaseParser): List<ScheduleDayResponse> {
        return jsonResponse.mapObjects { jsonItem ->
            val strDay = jsonItem.getString("day")
            val releases = releaseParser.releases(jsonItem.getJSONArray("items"))
            ScheduleDayResponse(strDay, releases)
        }
    }
}