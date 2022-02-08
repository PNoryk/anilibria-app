package tv.anilibria.feature.appupdates.data.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UpdateLinkResponse(
    @Json(name = "name") val name: String,
    @Json(name = "url") val url: String,
    @Json(name = "type") val type: String,
)