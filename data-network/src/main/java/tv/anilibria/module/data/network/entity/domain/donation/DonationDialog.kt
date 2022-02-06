package tv.anilibria.module.data.network.entity.domain.donation

data class DonationDialog(
    val tag: String,
    val content: List<DonationContentItem>,
    val cancelText: String?
)