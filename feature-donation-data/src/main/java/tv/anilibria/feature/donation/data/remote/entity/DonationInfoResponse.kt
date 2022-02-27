package tv.anilibria.feature.donation.data.remote.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DonationInfoResponse(
    @Json(name = "cards")
    val cards: DonationCardsResponse,
    @Json(name = "detail")
    val detail: DonationDetailResponse
)
