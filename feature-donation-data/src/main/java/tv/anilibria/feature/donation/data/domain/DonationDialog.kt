package tv.anilibria.feature.donation.data.domain

data class DonationDialog(
    val tag: String,
    val content: List<DonationContentItem>,
    val cancelText: String?
)