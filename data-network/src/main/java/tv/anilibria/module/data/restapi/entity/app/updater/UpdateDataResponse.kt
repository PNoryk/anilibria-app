package tv.anilibria.module.data.restapi.entity.app.updater

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created by radiationx on 28.01.18.
 */
@JsonClass(generateAdapter = true)
data class UpdateDataResponse(
    @Json(name = "version_code") val code: Int,
    @Json(name = "version_build") val build: Int,
    @Json(name = "version_name") val name: String,
    @Json(name = "build_date") val date: String,
    @Json(name = "links") val links: List<UpdateLinkResponse>,
    @Json(name = "important") val important: List<String>,
    @Json(name = "added") val added: List<String>,
    @Json(name = "fixed") val fixed: List<String>,
    @Json(name = "changed") val changed: List<String>,
)