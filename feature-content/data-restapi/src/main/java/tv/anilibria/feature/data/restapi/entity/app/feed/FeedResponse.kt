package tv.anilibria.feature.data.restapi.entity.app.feed

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import tv.anilibria.feature.data.restapi.entity.app.release.ReleaseResponse
import tv.anilibria.feature.data.restapi.entity.app.youtube.YoutubeResponse

@JsonClass(generateAdapter = true)
data class FeedResponse(
    @Json(name = "release") val release: ReleaseResponse?,
    @Json(name = "youtube") val youtube: YoutubeResponse?
)