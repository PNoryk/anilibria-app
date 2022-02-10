package tv.anilibria.module.data.local.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LinkMenuItemLocal(
    @Json(name = "title") val title : String,
    @Json(name = "absoluteLink") val absoluteLink : String?,
    @Json(name = "sitePagePath") val sitePagePath : String?,
    @Json(name = "icon") val icon : String?
)