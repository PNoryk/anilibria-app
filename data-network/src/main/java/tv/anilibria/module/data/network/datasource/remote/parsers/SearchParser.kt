package tv.anilibria.module.data.network.datasource.remote.parsers

import org.json.JSONArray
import ru.radiationx.shared.ktx.android.mapStrings
import ru.radiationx.shared.ktx.android.nullString
import tv.anilibria.module.data.network.datasource.remote.IApiUtils
import tv.anilibria.module.data.network.datasource.remote.address.ApiConfig
import tv.anilibria.module.data.network.entity.app.search.SuggestionItemResponse
import javax.inject.Inject

class SearchParser @Inject constructor(
    private val apiUtils: IApiUtils,
    private val apiConfig: ApiConfig
) {

    fun fastSearch(jsonResponse: JSONArray): List<SuggestionItemResponse> {
        val result: MutableList<SuggestionItemResponse> = mutableListOf()
        for (i in 0 until jsonResponse.length()) {
            val jsonItem = jsonResponse.getJSONObject(i)
            val names = jsonItem.getJSONArray("names").let { names ->
                (0 until names.length()).map {
                    apiUtils.escapeHtml(names.getString(it)).toString()
                }
            }
            val item = SuggestionItemResponse(
                jsonItem.getInt("id"),
                jsonItem.getString("code"),
                names,
                "${apiConfig.baseImagesUrl}${jsonItem.nullString("poster")}"
            )
            result.add(item)
        }
        return result
    }

    fun years(jsonResponse: JSONArray): List<String> {
        return jsonResponse.mapStrings { it }
    }

    fun genres(jsonResponse: JSONArray): List<String> {
        return jsonResponse.mapStrings { it }
    }

}