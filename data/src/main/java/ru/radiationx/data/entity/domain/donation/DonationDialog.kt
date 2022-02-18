package ru.radiationx.data.entity.domain.donation

@Deprecated("old data")
data class DonationDialog(
    val tag: String,
    val content: List<DonationContentItem>,
    val cancelText: String?
)