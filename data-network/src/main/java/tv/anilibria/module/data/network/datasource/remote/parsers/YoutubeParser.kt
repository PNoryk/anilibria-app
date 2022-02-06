package tv.anilibria.module.data.network.datasource.remote.parsers

import org.json.JSONObject
import tv.anilibria.module.data.network.datasource.remote.IApiUtils
import tv.anilibria.module.data.network.datasource.remote.address.ApiConfig
import tv.anilibria.module.data.network.entity.app.Paginated
import tv.anilibria.module.data.network.entity.app.youtube.YoutubeResponse
import ru.radiationx.shared.ktx.android.nullGet
import ru.radiationx.shared.ktx.android.nullString
import javax.inject.Inject

class YoutubeParser @Inject constructor(
        private val apiUtils: IApiUtils,
        private val apiConfig: ApiConfig
) {

    fun youtube(jsonItem: JSONObject): YoutubeResponse {
        val item = YoutubeResponse()
        item.id = jsonItem.getInt("id")
        item.title = apiUtils.escapeHtml(jsonItem.nullString("title"))
        item.image = "${apiConfig.baseImagesUrl}${jsonItem.nullString("image")}"
        item.vid = jsonItem.nullString("vid")
        item.views = jsonItem.getInt("views")
        item.comments = jsonItem.getInt("comments")
        item.timestamp = jsonItem.getInt("timestamp")
        return item
    }

    fun parse(jsonResponse: JSONObject): Paginated<List<YoutubeResponse>> {
        val result = mutableListOf<YoutubeResponse>()
        val jsonItems = jsonResponse.getJSONArray("items")
        for (i in 0 until jsonItems.length()) {
            val jsonItem = jsonItems.getJSONObject(i)
            val item = youtube(jsonItem)
            result.add(item)
        }

        val pagination = Paginated(result)
        val jsonNav = jsonResponse.getJSONObject("pagination")
        jsonNav.nullGet("page")?.let { pagination.page = it.toString().toInt() }
        jsonNav.nullGet("perPage")?.let { pagination.perPage = it.toString().toInt() }
        jsonNav.nullGet("allPages")?.let { pagination.allPages = it.toString().toInt() }
        jsonNav.nullGet("allItems")?.let { pagination.allItems = it.toString().toInt() }
        return pagination
    }
}