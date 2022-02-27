package tv.anilibria.feature.auth.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OtpInfoResponse(
    @Json(name = "code") val code: String,
    @Json(name = "description") val description: String,
    @Json(name = "expiredAt") val expiresAt: Long,
    @Json(name = "remainingTime") val remainingTime: Long
)