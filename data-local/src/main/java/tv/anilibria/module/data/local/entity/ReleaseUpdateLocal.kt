package tv.anilibria.module.data.local.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ReleaseUpdateLocal(
    @Json(name = "id") val id : Long,
    @Json(name = "lastKnownUpdateAt") val lastKnownUpdateAt : Long,
)