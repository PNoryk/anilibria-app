package tv.anilibria.module.data.network.entity.app.donation

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DonationCardsResponse(
    @Json(name = "new_donations")
    val newDonations: DonationCardResponse?,
    @Json(name = "release")
    val release: DonationCardResponse?
)