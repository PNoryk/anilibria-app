package ru.radiationx.data.entity.domain.donation

@Deprecated("old data")
sealed class DonationContentItem

@Deprecated("old data")
data class DonationContentButton(
    val tag: String?,
    val text: String,
    val link: String?,
    val brand: String?,
    val icon: String?
) : DonationContentItem()

@Deprecated("old data")
data class DonationContentCaption(
    val text: String
) : DonationContentItem()

@Deprecated("old data")
data class DonationContentDivider(
    val height: Int
) : DonationContentItem()

@Deprecated("old data")
data class DonationContentHeader(
    val text: String
) : DonationContentItem()

@Deprecated("old data")
data class DonationContentSection(
    val title: String?,
    val subtitle: String?
) : DonationContentItem()