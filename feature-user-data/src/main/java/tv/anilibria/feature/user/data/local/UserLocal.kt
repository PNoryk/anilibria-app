package tv.anilibria.feature.user.data.local

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserLocal(
    @Json(name = "id") val id: Long,
    @Json(name = "avatar") val avatar: String?,
    @Json(name = "login") val login: String
)