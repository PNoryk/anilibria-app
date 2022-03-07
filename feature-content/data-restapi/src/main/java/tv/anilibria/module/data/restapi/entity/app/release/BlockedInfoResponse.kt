package tv.anilibria.module.data.restapi.entity.app.release

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BlockedInfoResponse(
    @Json(name = "blocked") val isBlocked: Boolean,
    @Json(name = "reason") val reason: String?
)