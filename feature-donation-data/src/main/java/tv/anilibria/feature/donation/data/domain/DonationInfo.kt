package tv.anilibria.feature.donation.data.domain

import tv.anilibria.feature.donation.data.domain.yoomoney.YooMoneyDialog


data class DonationInfo(
    val cardNewDonations: DonationCard?,
    val cardRelease: DonationCard?,
    val detailContent: List<DonationContentItem>,
    val contentDialogs: List<DonationDialog>,
    val yooMoneyDialog: YooMoneyDialog?
) {
    companion object {
        const val YOOMONEY_TAG = "yoomoney_dialog"
    }
}
