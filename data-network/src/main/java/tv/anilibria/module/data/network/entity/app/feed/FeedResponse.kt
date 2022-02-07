package tv.anilibria.module.data.network.entity.app.feed

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import tv.anilibria.module.data.network.entity.app.release.ReleaseResponse
import tv.anilibria.module.data.network.entity.app.youtube.YoutubeResponse

@JsonClass(generateAdapter = true)
data class FeedResponse(
    @Json(name = "release") val release: ReleaseResponse?,
    @Json(name = "youtube") val youtube: YoutubeResponse?
)