package tv.anilibria.module.domain.entity.donation

import tv.anilibria.module.domain.entity.donation.yoomoney.YooMoneyDialog


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
