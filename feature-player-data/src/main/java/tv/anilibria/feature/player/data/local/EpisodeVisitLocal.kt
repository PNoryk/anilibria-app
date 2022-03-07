package tv.anilibria.feature.player.data.local

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EpisodeVisitLocal(
    @Json(name = "id") val id: Long,
    @Json(name = "releaseId") val releaseId: Long,
    @Json(name = "playerSeek") val playerSeek: Long?,
    @Json(name = "lastOpenAt") val lastOpenAt: Long?
)