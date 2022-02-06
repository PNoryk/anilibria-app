package tv.anilibria.module.data.network.datasource.remote.parsers

import org.json.JSONArray
import tv.anilibria.module.data.network.datasource.remote.IApiUtils
import tv.anilibria.module.data.network.datasource.remote.address.ApiConfig
import tv.anilibria.module.data.network.entity.app.release.GenreItemResponse
import tv.anilibria.module.data.network.entity.app.release.YearItemResponse
import tv.anilibria.module.data.network.entity.app.search.SuggestionItemResponse
import ru.radiationx.shared.ktx.android.nullString
import javax.inject.Inject

class SearchParser @Inject constructor(
        private val apiUtils: IApiUtils,
        private val apiConfig: ApiConfig
) {

    fun fastSearch(jsonResponse: JSONArray): List<SuggestionItemResponse> {
        val result: MutableList<SuggestionItemResponse> = mutableListOf()
        for (i in 0 until jsonResponse.length()) {
            val jsonItem = jsonResponse.getJSONObject(i)
            val item = SuggestionItemResponse()

            item.id = jsonItem.getInt("id")
            item.code = jsonItem.getString("code")
            item.names.addAll(jsonItem.getJSONArray("names").let { names ->
                (0 until names.length()).map {
                    apiUtils.escapeHtml(names.getString(it)).toString()
                }
            })
            item.poster = "${apiConfig.baseImagesUrl}${jsonItem.nullString("poster")}"
            result.add(item)
        }
        return result
    }

    fun years(jsonResponse: JSONArray): List<YearItemResponse> {
        val result: MutableList<YearItemResponse> = mutableListOf()
        for (i in 0 until jsonResponse.length()) {
            val yearText = jsonResponse.getString(i)
            val genreItem = YearItemResponse().apply {
                title = yearText
                value = yearText
            }
            result.add(genreItem)
        }
        return result
    }

    fun genres(jsonResponse: JSONArray): List<GenreItemResponse> {
        val result: MutableList<GenreItemResponse> = mutableListOf()
        for (i in 0 until jsonResponse.length()) {
            val genreText = jsonResponse.getString(i)
            val genreItem = GenreItemResponse().apply {
                title = genreText.capitalize()
                value = genreText
            }
            result.add(genreItem)
        }
        return result
    }

}