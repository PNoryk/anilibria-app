package tv.anilibria.module.data.network.datasource.remote.parsers

import org.json.JSONArray
import org.json.JSONObject
import ru.radiationx.shared.ktx.android.mapObjects
import ru.radiationx.shared.ktx.android.nullString
import tv.anilibria.module.data.network.entity.app.other.LinkMenuItemResponse
import javax.inject.Inject

class MenuParser @Inject constructor() {

    fun parse(responseJson: JSONArray): List<LinkMenuItemResponse> {
        val result = responseJson.mapObjects {
            parseItem(it)
        }
        return result
    }

    fun parseItem(jsonItem: JSONObject): LinkMenuItemResponse = LinkMenuItemResponse(
        jsonItem.getString("title"),
        jsonItem.nullString("absoluteLink"),
        jsonItem.getString("sitePagePath"),
        jsonItem.getString("icon")
    )

}