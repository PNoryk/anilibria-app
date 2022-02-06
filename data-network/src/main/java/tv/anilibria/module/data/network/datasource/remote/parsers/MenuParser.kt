package tv.anilibria.module.data.network.datasource.remote.parsers

import org.json.JSONArray
import org.json.JSONObject
import ru.radiationx.shared.ktx.android.nullString
import tv.anilibria.module.data.network.entity.app.other.LinkMenuItemResponse
import javax.inject.Inject

class MenuParser @Inject constructor() {

    fun parse(responseJson: JSONArray): List<LinkMenuItemResponse> {
        val result = mutableListOf<LinkMenuItemResponse>()
        for (i in 0 until responseJson.length()) {
            responseJson.optJSONObject(i)?.let { addressJson ->
                result.add(parseItem(addressJson))
            }
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