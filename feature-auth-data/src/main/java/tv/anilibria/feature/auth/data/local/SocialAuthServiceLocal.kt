package tv.anilibria.feature.auth.data.local

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SocialAuthServiceLocal(
    @Json(name = "key") val key : String,
    @Json(name = "title") val title : String,
    @Json(name = "socialUrl") val socialUrl : String,
    @Json(name = "resultPattern") val resultPattern : String,
    @Json(name = "errorUrlPattern") val errorUrlPattern : String,
    @Json(name = "color") val color : String,
    @Json(name = "icon") val icon : String
)