package tv.anilibria.feature.content.data.restapi.entity.app.schedule

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import tv.anilibria.feature.content.data.restapi.entity.app.release.ReleaseResponse

@JsonClass(generateAdapter = true)
data class ScheduleDayResponse(
    @Json(name = "day") val day: String,
    @Json(name = "items") val items: List<ReleaseResponse>
)