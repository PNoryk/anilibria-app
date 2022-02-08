package tv.anilibria.module.data.restapi.entity.app.donation

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import tv.anilibria.module.data.restapi.entity.app.donation.content_data.DonationDialogResponse
import tv.anilibria.module.data.restapi.entity.app.donation.content_data.YooMoneyDialogResponse

@JsonClass(generateAdapter = true)
data class DonationDetailResponse(
    @Json(name = "content")
    val content: List<DonationContentItemResponse>,
    @Json(name = "yoomoney_dialog")
    val yooMoneyDialog: YooMoneyDialogResponse?,
    @Json(name = "content_dialogs")
    val contentDialogs: List<DonationDialogResponse>
)
