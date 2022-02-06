package tv.anilibria.module.domain.entity.donation

data class DonationDialog(
    val tag: String,
    val content: List<DonationContentItem>,
    val cancelText: String?
)