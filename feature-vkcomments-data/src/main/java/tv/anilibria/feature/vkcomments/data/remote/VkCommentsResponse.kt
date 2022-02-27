package tv.anilibria.feature.vkcomments.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VkCommentsResponse(
    @Json(name = "baseUrl") val baseUrl: String,
    @Json(name = "script") val script: String
)