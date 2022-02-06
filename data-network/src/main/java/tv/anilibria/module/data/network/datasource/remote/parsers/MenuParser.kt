package tv.anilibria.module.data.network.datasource.remote.parsers

import org.json.JSONArray
import org.json.JSONObject
import tv.anilibria.module.data.network.entity.app.other.LinkMenuItem
import ru.radiationx.shared.ktx.android.nullString
import javax.inject.Inject

class MenuParser @Inject constructor() {

    fun parse(responseJson: JSONArray): List<LinkMenuItem> {
        val result = mutableListOf<LinkMenuItem>()
        for (i in 0 until responseJson.length()) {
            responseJson.optJSONObject(i)?.let { addressJson ->
                result.add(parseItem(addressJson))
            }
        }
        return result
    }

    fun parseItem(jsonItem: JSONObject): LinkMenuItem = LinkMenuItem(
            jsonItem.getString("title"),
            jsonItem.nullString("absoluteLink"),
            jsonItem.getString("sitePagePath"),
            jsonItem.getString("icon")
    )

}