package tv.anilibria.feature.donation.data.remote.entity.content_data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import tv.anilibria.feature.donation.data.remote.entity.DonationContentItemResponse

@JsonClass(generateAdapter = true)
data class DonationDialogResponse(
    @Json(name = "tag")
    val tag: String,
    @Json(name = "content")
    val content: List<DonationContentItemResponse>,
    @Json(name = "cancel_text")
    val cancelText: String?
)