package tv.anilibria.module.data.network.entity.app.release

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EpisodeResponse(
    @Json(name = "id") val id: Long,
    @Json(name = "title") val title: String?,
    @Json(name = "sd") val urlSd: String?,
    @Json(name = "hd") val urlHd: String?,
    @Json(name = "fullhd") val urlFullHd: String?,
    @Json(name = "srcSd") val srcUrlSd: String?,
    @Json(name = "srcHd") val srcUrlHd: String?,
    @Json(name = "srcFullHd") val srcUrlFullHd: String?
)