package tv.anilibria.module.data.network.entity.app.other

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserResponse(
    @Json(name = "id") val id: Long,
    @Json(name = "avatar") val avatar: String?,
    @Json(name = "login") val login: String?
)
