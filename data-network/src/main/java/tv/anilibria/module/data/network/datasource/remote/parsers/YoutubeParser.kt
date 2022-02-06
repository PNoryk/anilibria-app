package tv.anilibria.module.data.network.datasource.remote.parsers

import org.json.JSONObject
import ru.radiationx.shared.ktx.android.mapObjects
import ru.radiationx.shared.ktx.android.nullGet
import ru.radiationx.shared.ktx.android.nullString
import tv.anilibria.module.data.network.entity.app.PaginatedResponse
import tv.anilibria.module.data.network.entity.app.youtube.YoutubeResponse
import javax.inject.Inject

class YoutubeParser @Inject constructor() {

    fun youtube(jsonItem: JSONObject): YoutubeResponse {
        return YoutubeResponse(
            jsonItem.getInt("id"),
            jsonItem.nullString("title"),
            jsonItem.nullString("image"),
            jsonItem.nullString("vid"),
            jsonItem.getInt("views"),
            jsonItem.getInt("comments"),
            jsonItem.getInt("timestamp"),
        )
    }

    fun parse(jsonResponse: JSONObject): PaginatedResponse<List<YoutubeResponse>> {
        val result = jsonResponse.getJSONArray("items").mapObjects { youtube(it) }
        val pagination = PaginatedResponse(result)
        val jsonNav = jsonResponse.getJSONObject("pagination")
        jsonNav.nullGet("page")?.let { pagination.page = it.toString().toInt() }
        jsonNav.nullGet("perPage")?.let { pagination.perPage = it.toString().toInt() }
        jsonNav.nullGet("allPages")?.let { pagination.allPages = it.toString().toInt() }
        jsonNav.nullGet("allItems")?.let { pagination.allItems = it.toString().toInt() }
        return pagination
    }
}