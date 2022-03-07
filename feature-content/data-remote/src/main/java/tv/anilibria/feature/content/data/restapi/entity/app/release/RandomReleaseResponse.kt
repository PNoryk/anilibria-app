package tv.anilibria.feature.content.data.restapi.entity.app.release

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RandomReleaseResponse(
    @Json(name = "code") val code: String
)