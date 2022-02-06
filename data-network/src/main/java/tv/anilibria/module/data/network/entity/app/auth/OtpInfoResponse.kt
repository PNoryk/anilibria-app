package tv.anilibria.module.data.network.entity.app.auth

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OtpInfoResponse(
    @Json(name = "code") val code: String,
    @Json(name = "description") val description: String,
    @Json(name = "expiredAt") val expiresAt: Int,
    @Json(name = "remainingTime") val remainingTime: Int
)