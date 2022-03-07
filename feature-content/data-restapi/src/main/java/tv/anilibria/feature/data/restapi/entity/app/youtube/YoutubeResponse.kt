package tv.anilibria.feature.data.restapi.entity.app.youtube

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class YoutubeResponse(
    @Json(name = "id") val id: Long,
    @Json(name = "title") val title: String?,
    @Json(name = "image") val image: String?,
    @Json(name = "vid") val vid: String?,
    @Json(name = "views") val views: Long,
    @Json(name = "comments") val comments: Long,
    @Json(name = "timestamp") val timestamp: Long
) 