package tv.anilibria.module.data.network.datasource.remote.parsers

import org.json.JSONObject
import ru.radiationx.shared.ktx.android.nullGet
import ru.radiationx.shared.ktx.android.nullString
import tv.anilibria.module.data.network.datasource.remote.IApiUtils
import tv.anilibria.module.data.network.datasource.remote.address.ApiConfig
import tv.anilibria.module.data.network.entity.app.PaginatedResponse
import tv.anilibria.module.data.network.entity.app.youtube.YoutubeResponse
import javax.inject.Inject

class YoutubeParser @Inject constructor(
    private val apiUtils: IApiUtils,
    private val apiConfig: ApiConfig
) {

    fun youtube(jsonItem: JSONObject): YoutubeResponse {
        return YoutubeResponse(
            jsonItem.getInt("id"),
            apiUtils.escapeHtml(jsonItem.nullString("title")),
            "${apiConfig.baseImagesUrl}${jsonItem.nullString("image")}",
            jsonItem.nullString("vid"),
            jsonItem.getInt("views"),
            jsonItem.getInt("comments"),
            jsonItem.getInt("timestamp"),
        )
    }

    fun parse(jsonResponse: JSONObject): PaginatedResponse<List<YoutubeResponse>> {
        val result = mutableListOf<YoutubeResponse>()
        val jsonItems = jsonResponse.getJSONArray("items")
        for (i in 0 until jsonItems.length()) {
            val jsonItem = jsonItems.getJSONObject(i)
            val item = youtube(jsonItem)
            result.add(item)
        }

        val pagination = PaginatedResponse(result)
        val jsonNav = jsonResponse.getJSONObject("pagination")
        jsonNav.nullGet("page")?.let { pagination.page = it.toString().toInt() }
        jsonNav.nullGet("perPage")?.let { pagination.perPage = it.toString().toInt() }
        jsonNav.nullGet("allPages")?.let { pagination.allPages = it.toString().toInt() }
        jsonNav.nullGet("allItems")?.let { pagination.allItems = it.toString().toInt() }
        return pagination
    }
}