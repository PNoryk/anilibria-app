package tv.anilibria.module.data.network.datasource.remote.parsers

import org.json.JSONObject
import tv.anilibria.module.data.network.datasource.remote.IApiUtils
import tv.anilibria.module.data.network.entity.app.page.PageLibriaResponse
import tv.anilibria.module.data.network.entity.app.page.VkCommentsResponse
import java.util.regex.Pattern
import javax.inject.Inject

/**
 * Created by radiationx on 13.01.18.
 */
class PagesParser @Inject constructor(
        private val apiUtils: IApiUtils
) {

    private val pagePatternSource = "(<div[^>]*?class=\"[^\"]*?news-body[^\"]*?\"[^>]*?>[\\s\\S]*?<\\/div>)[^<]*?<div[^>]*?(?:id=\"vk_comments|class=\"[^\"]*?side[^\"]*?\")"
    private val titlePatternSource = "<title>([\\s\\S]*?)<\\/title>"

    private val pagePattern: Pattern by lazy {
        Pattern.compile(pagePatternSource, Pattern.CASE_INSENSITIVE)
    }

    private val titlePattern: Pattern by lazy {
        Pattern.compile(titlePatternSource, Pattern.CASE_INSENSITIVE)
    }

    fun baseParse(httpResponse: String): PageLibriaResponse {
        val result = PageLibriaResponse()
        var matcher = pagePattern.matcher(httpResponse)
        var content = ""
        while (matcher.find()) {
            content += matcher.group(1)
        }
        matcher = titlePattern.matcher(httpResponse)
        if (matcher.find()) {
            result.title = matcher.group(1)
        }
        result.content = content
        return result
    }

    fun parseVkComments(jsonResponse: JSONObject): VkCommentsResponse {
        return VkCommentsResponse(
                jsonResponse.getString("baseUrl"),
                jsonResponse.getString("script")
        )
    }
}