package tv.anilibria.module.data.network.datasource.remote.parsers

import org.json.JSONArray
import ru.radiationx.shared.ktx.android.mapStrings
import javax.inject.Inject

class SearchParser @Inject constructor() {


    fun years(jsonResponse: JSONArray): List<String> {
        return jsonResponse.mapStrings { it }
    }

    fun genres(jsonResponse: JSONArray): List<String> {
        return jsonResponse.mapStrings { it }
    }

}