package tv.anilibria.module.data.network.entity.app.donation.content

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DonationContentCaptionResponse(
    @Json(name = "text")
    val text: String
)