package tv.anilibria.module.data.network.entity.app.other

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LinkMenuItemResponse(
    @Json(name = "title") val title: String,
    @Json(name = "absoluteLink") val absoluteLink: String?,
    @Json(name = "sitePagePath") val sitePagePath: String?,
    @Json(name = "icon") val icon: String?
)