package tv.anilibria.feature.donation.data.remote.entity.content

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DonationContentCaptionResponse(
    @Json(name = "text")
    val text: String
)