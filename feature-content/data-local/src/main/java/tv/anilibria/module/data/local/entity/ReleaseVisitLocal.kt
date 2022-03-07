package tv.anilibria.module.data.local.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ReleaseVisitLocal(
    @Json(name = "id") val id: Long,
    @Json(name = "lastOpenAt") val lastOpenAt: Long,
)