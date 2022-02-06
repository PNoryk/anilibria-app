package tv.anilibria.module.data.network.datasource.remote.parsers

import org.json.JSONArray
import org.json.JSONObject
import tv.anilibria.module.data.network.datasource.remote.IApiUtils
import tv.anilibria.module.data.network.entity.app.feed.FeedResponse
import ru.radiationx.shared.ktx.android.nullGet
import javax.inject.Inject

class FeedParser @Inject constructor(
        private val apiUtils: IApiUtils
) {

    fun feed(
            jsonResponse: JSONArray,
            releaseParser: ReleaseParser,
            youtubeParser: YoutubeParser
    ): List<FeedResponse> {
        val result = mutableListOf<FeedResponse>()
        for (i in 0 until jsonResponse.length()) {
            val jsonItem = jsonResponse.getJSONObject(i)
            val jsonRelease = jsonItem.nullGet("release") as JSONObject?
            val jsonYoutube = jsonItem.nullGet("youtube") as JSONObject?
            val item = when {
                jsonRelease != null -> {
                    FeedResponse(release = releaseParser.parseRelease(jsonRelease))
                }
                jsonYoutube != null -> {
                    FeedResponse(youtube = youtubeParser.youtube(jsonYoutube))
                }
                else -> null
            }
            if (item != null) {
                result.add(item)
            }
        }
        return result
    }
}