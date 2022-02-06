package tv.anilibria.module.data.network.entity.app.release

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ExternalEpisodeResponse(
    @Json(name = "id") val id: Int,
    @Json(name = "title") val title: String?,
    @Json(name = "url") val url: String?
)