package tv.anilibria.module.data.network.datasource.remote.parsers

import org.json.JSONArray
import org.json.JSONObject
import ru.radiationx.shared.ktx.android.nullGet
import tv.anilibria.module.data.network.entity.app.PageMetaResponse
import tv.anilibria.module.data.network.entity.app.PageResponse

class PaginationParser {

    fun <T> parse(jsonResponse: JSONObject, itemsMapper: (JSONArray) -> List<T>): PageResponse<T> {
        val result = itemsMapper.invoke(jsonResponse.getJSONArray("items"))
        val meta = jsonResponse.getJSONObject("pagination").let { jsonNav ->
            PageMetaResponse(
                page = jsonNav.nullGet("page")?.toString()?.toInt(),
                perPage = jsonNav.nullGet("perPage")?.toString()?.toInt(),
                allPages = jsonNav.nullGet("allPages")?.toString()?.toInt(),
                allItems = jsonNav.nullGet("allItems")?.toString()?.toInt(),
            )
        }
        return PageResponse(result, meta)
    }
}