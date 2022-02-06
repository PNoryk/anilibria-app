package tv.anilibria.module.data.network.datasource.remote.parsers

import org.json.JSONObject
import ru.radiationx.shared.ktx.android.mapObjects
import ru.radiationx.shared.ktx.android.mapStrings
import tv.anilibria.module.data.network.entity.app.updater.UpdateDataResponse
import tv.anilibria.module.data.network.entity.app.updater.UpdateLinkResponse
import javax.inject.Inject

/**
 * Created by radiationx on 27.01.18.
 */
class CheckerParser @Inject constructor() {

    fun parse(responseJson: JSONObject): UpdateDataResponse {
        val jsonUpdate = responseJson.getJSONObject("update")

        val links = jsonUpdate.getJSONArray("links").mapObjects { linkJson ->
            UpdateLinkResponse(
                name = linkJson.getString("name"),
                url = linkJson.getString("url"),
                type = linkJson.getString("type")
            )
        }
        val important = jsonUpdate.getJSONArray("important").mapStrings { it }
        val added = jsonUpdate.getJSONArray("added").mapStrings { it }
        val fixed = jsonUpdate.getJSONArray("fixed").mapStrings { it }
        val changed = jsonUpdate.getJSONArray("changed").mapStrings { it }
        return UpdateDataResponse(
            code = jsonUpdate.getInt("version_code"),
            build = jsonUpdate.getInt("version_build"),
            name = jsonUpdate.getString("version_name"),
            date = jsonUpdate.getString("build_date"),
            links = links,
            important = important,
            added = added,
            fixed = fixed,
            changed = changed
        )
    }
}