package tv.anilibria.module.data.network.datasource.remote.parsers

import org.json.JSONObject
import ru.radiationx.shared.ktx.android.mapObjects
import ru.radiationx.shared.ktx.android.nullString
import tv.anilibria.module.data.network.entity.app.PageResponse
import tv.anilibria.module.data.network.entity.app.youtube.YoutubeResponse
import javax.inject.Inject

class YoutubeParser @Inject constructor(
    private val paginationParser: PaginationParser
) {

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

    fun parse(jsonResponse: JSONObject): PageResponse<YoutubeResponse> {
        return paginationParser.parse(jsonResponse) {
            it.mapObjects { youtube(it) }
        }
    }
}