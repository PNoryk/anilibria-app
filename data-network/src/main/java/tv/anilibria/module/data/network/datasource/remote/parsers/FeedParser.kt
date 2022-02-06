package tv.anilibria.module.data.network.datasource.remote.parsers

import org.json.JSONArray
import org.json.JSONObject
import ru.radiationx.shared.ktx.android.nullGet
import tv.anilibria.module.data.network.entity.app.feed.FeedResponse
import javax.inject.Inject

class FeedParser @Inject constructor() {

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
            val item = FeedResponse(
                release = jsonRelease?.let { releaseParser.parseRelease(it) },
                youtube = jsonYoutube?.let { youtubeParser.youtube(it) }
            )
            result.add(item)
        }
        return result
    }
}