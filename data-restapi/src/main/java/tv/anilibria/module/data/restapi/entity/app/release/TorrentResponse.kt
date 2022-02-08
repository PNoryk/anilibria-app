package tv.anilibria.module.data.restapi.entity.app.release

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created by radiationx on 30.01.18.
 */
@JsonClass(generateAdapter = true)
data class TorrentResponse(
    @Json(name = "id") val id: Long,
    @Json(name = "hash") val hash: String?,
    @Json(name = "leechers") val leechers: Long,
    @Json(name = "seeders") val seeders: Long,
    @Json(name = "completed") val completed: Long,
    @Json(name = "quality") val quality: String?,
    @Json(name = "series") val series: String?,
    @Json(name = "size") val size: Long,
    @Json(name = "url") val url: String?
)